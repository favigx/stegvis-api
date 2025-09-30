package com.stegvis_api.stegvis_api.integration.uhr.service;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.integration.uhr.client.UHRHttpClient;
import com.stegvis_api.stegvis_api.integration.uhr.model.ProgramResponse;
import com.stegvis_api.stegvis_api.integration.uhr.dto.EligibleProgramResponse;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UHRService {

    private final UHRHttpClient uhrHttpClient;
    private final UserService userService;

    public UHRService(UHRHttpClient uhrHttpClient, UserService userService) {
        this.uhrHttpClient = uhrHttpClient;
        this.userService = userService;
    }

    public List<EligibleProgramResponse> getEligibleProgramsForUser(String userId, String searchFor) {
        User user = userService.getUserByIdOrThrow(userId);
        double meritValue = user.getMeritValue();

        String[] senasteTerminer = { "HT24", "VT24", "HT23", "VT23" };
        List<EligibleProgramResponse> eligiblePrograms = new ArrayList<>();

        for (String termin : senasteTerminer) {
            try {
                ProgramResponse response = uhrHttpClient.searchTotal(
                        searchFor,
                        termin,
                        "",
                        1000,
                        1,
                        "urval2");

                if (response.getData() == null || response.getData().isEmpty()) {
                    continue;
                }

                List<EligibleProgramResponse> filtered = response.getData().stream()
                        .filter(p -> isEligible(p, meritValue))
                        .map(p -> new EligibleProgramResponse(
                                p.getLärosäte(),
                                p.getStudieort(),
                                p.getAnmälningsalternativ(),
                                getLowestScore(p.getUrval1()),
                                getLowestScore(p.getUrval2())))
                        .collect(Collectors.toList());

                eligiblePrograms.addAll(filtered);

            } catch (HttpClientErrorException.NotFound e) {
                log.warn("UHR API returned 404 for searchFor={} and termin={}", searchFor, termin);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                log.error("Error calling UHR API for searchFor={} and termin={}: {}", searchFor, termin, e.getMessage(),
                        e);
            }
        }

        if (eligiblePrograms.isEmpty()) {
            throw new ResourceNotFoundException("Inga program matchade användarens meritvärde");
        }

        return eligiblePrograms;
    }

    private boolean isEligible(ProgramResponse.Program program, double meritValue) {
        List<String> allowedGroups = List.of("BI", "BII");

        boolean eligibleUrval1 = program.getUrval1().getUrvalsgrupper().stream()
                .filter(u -> allowedGroups.contains(u.getUrvalsgruppId()))
                .anyMatch(u -> isScoreEligible(u.getLägstaAntagnaPoäng(), meritValue));

        boolean eligibleUrval2 = program.getUrval2().getUrvalsgrupper().stream()
                .filter(u -> allowedGroups.contains(u.getUrvalsgruppId()))
                .anyMatch(u -> isScoreEligible(u.getLägstaAntagnaPoäng(), meritValue));

        return eligibleUrval1 || eligibleUrval2;
    }

    private String getLowestScore(ProgramResponse.Urval urval) {
        if (urval == null || urval.getUrvalsgrupper() == null || urval.getUrvalsgrupper().isEmpty()) {
            return "-";
        }

        List<String> allowedGroups = List.of("BI", "BII");

        return urval.getUrvalsgrupper().stream()
                .filter(u -> allowedGroups.contains(u.getUrvalsgruppId()))
                .map(ProgramResponse.Urvalsgrupp::getLägstaAntagnaPoäng)
                .findFirst()
                .orElse("-");
    }

    private boolean isScoreEligible(String lowestScoreStr, double userMerit) {
        try {
            double score = Double.parseDouble(lowestScoreStr.replace("*", "0").replace("-", "0"));
            return userMerit >= score;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}