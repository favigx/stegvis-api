package com.stegvis_api.stegvis_api.integration.uhr.service;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.integration.uhr.client.UHRHttpClient;
import com.stegvis_api.stegvis_api.integration.uhr.model.ProgramResponse;
import com.stegvis_api.stegvis_api.integration.uhr.dto.EligibleProgramResponse;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UHRService {

    private final UHRHttpClient uhrHttpClient;
    private final UserService userService;

    public List<EligibleProgramResponse> getEligibleProgramsForUser(String userId, String searchFor) {
        User user = userService.getUserByIdOrThrow(userId);
        double meritValue = user.getUserPreference().getMeritValue();

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
                        .filter(p -> isEligibleBI_BII(p, meritValue))
                        .map(p -> new EligibleProgramResponse(
                                p.getLärosäte(),
                                p.getStudieort(),
                                p.getAnmälningsalternativ(),
                                getLowestScoreBI_BII(p.getUrval1()),
                                getLowestScoreBI_BII(p.getUrval2())))
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

    private boolean isEligibleBI_BII(ProgramResponse.Program program, double meritValue) {
        return checkUrval(program.getUrval1(), meritValue)
                || checkUrval(program.getUrval2(), meritValue);
    }

    private boolean checkUrval(ProgramResponse.Urval urval, double meritValue) {
        if (urval == null || urval.getUrvalsgrupper() == null)
            return false;

        return urval.getUrvalsgrupper().stream()
                .filter(u -> "BI".equals(u.getUrvalsgruppId()) || "BII".equals(u.getUrvalsgruppId()))
                .anyMatch(u -> isScoreEligible(u.getLägstaAntagnaPoäng(), meritValue));
    }

    private String getLowestScoreBI_BII(ProgramResponse.Urval urval) {
        if (urval == null || urval.getUrvalsgrupper() == null)
            return "-";

        return urval.getUrvalsgrupper().stream()
                .filter(u -> "BI".equals(u.getUrvalsgruppId()) || "BII".equals(u.getUrvalsgruppId()))
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