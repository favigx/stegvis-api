package com.stegvis_api.stegvis_api.integration.uhr.service;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.integration.uhr.client.UHRHttpClient;
import com.stegvis_api.stegvis_api.integration.uhr.model.ProgramResponse;
import com.stegvis_api.stegvis_api.integration.uhr.model.ProgramResponse.Program;
import com.stegvis_api.stegvis_api.integration.uhr.dto.EligibleProgramResponse;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.*;
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

        List<String> senasteTerminer = getSenasteTerminer();

        List<ProgramResponse.Program> allPrograms = fetchPrograms(searchFor, senasteTerminer);

        if (allPrograms.isEmpty()) {
            throw new ResourceNotFoundException("Inga program matchade användarens meritvärde");
        }

        List<ProgramResponse.Program> filteredPrograms = filterPrograms(allPrograms, meritValue);

        return buildEligibleProgramResponses(filteredPrograms);
    }

    private List<String> getSenasteTerminer() {
        int currentYear = Year.now().getValue();
        String yearStr = String.valueOf(currentYear).substring(2);
        String lastYearStr = String.valueOf(currentYear - 1).substring(2);
        String twoYearsAgoStr = String.valueOf(currentYear - 2).substring(2);

        return Arrays.asList(
                "HT" + yearStr,
                "VT" + yearStr,
                "HT" + lastYearStr,
                "VT" + lastYearStr,
                "HT" + twoYearsAgoStr,
                "VT" + twoYearsAgoStr);
    }

    private List<ProgramResponse.Program> fetchPrograms(String searchFor, List<String> terminer) {
        List<ProgramResponse.Program> allPrograms = new ArrayList<>();
        for (String termin : terminer) {
            try {
                ProgramResponse response = uhrHttpClient.searchTotal(
                        searchFor,
                        termin,
                        "",
                        1000,
                        1,
                        "urval2");
                if (response.getData() != null && !response.getData().isEmpty()) {
                    allPrograms.addAll(response.getData());
                }
            } catch (HttpClientErrorException.NotFound e) {
                log.warn("UHR API returned 404 for searchFor={} and termin={}", searchFor, termin);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                log.error("Error calling UHR API for searchFor={} and termin={}: {}", searchFor, termin, e.getMessage(),
                        e);
            }
        }
        return allPrograms;
    }

    private List<ProgramResponse.Program> filterPrograms(List<ProgramResponse.Program> programs, double meritValue) {
        return programs.stream()
                .filter(p -> !hasOnlyAsteriskScores(p) && isEligibleByMerit(p, meritValue))
                .sorted(Comparator.comparingInt(p -> {
                    ProgramResponse.Sok sok = ((Program) p).getSok();
                    return sok != null ? sok.getSökande() : 0;
                }).reversed())
                .collect(Collectors.toList());
    }

    private List<EligibleProgramResponse> buildEligibleProgramResponses(List<ProgramResponse.Program> programs) {
        Map<String, Map<String, EligibleProgramResponse.TermData>> programTermMap = new LinkedHashMap<>();
        Map<String, String> programOrtMap = new HashMap<>();

        for (ProgramResponse.Program p : programs) {
            String key = p.getLärosäte() + "|" + p.getAnmälningsalternativ();
            programTermMap.putIfAbsent(key, new LinkedHashMap<>());
            programOrtMap.putIfAbsent(key, p.getStudieort());

            programTermMap.get(key).put(p.getTerminId(), EligibleProgramResponse.TermData.builder()
                    .termin(p.getTermin())
                    .termId(p.getTerminId())
                    .lägstaPoängUrval1(getLowestScoreBI_BII(p.getUrval1()))
                    .lägstaPoängUrval2(getLowestScoreBI_BII(p.getUrval2()))
                    .build());
        }

        return programTermMap.entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split("\\|");
                    String universitet = parts[0];
                    String programnamn = parts[1];
                    String ort = programOrtMap.get(entry.getKey());
                    String antagningUrl = getAntagningsUrl(programnamn, ort);

                    return EligibleProgramResponse.builder()
                            .universitet(universitet)
                            .programnamn(programnamn)
                            .ort(ort)
                            .terminer(entry.getValue())
                            .antagningUrl(antagningUrl)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private boolean isEligibleByMerit(ProgramResponse.Program program, double meritValue) {
        String score1 = getLowestScoreBI_BII(program.getUrval1());
        String score2 = getLowestScoreBI_BII(program.getUrval2());
        return isScoreEligible(score1, meritValue) || isScoreEligible(score2, meritValue);
    }

    private boolean isScoreEligible(String lowestScoreStr, double userMerit) {
        if (lowestScoreStr == null || lowestScoreStr.isBlank() || lowestScoreStr.equals("*")
                || lowestScoreStr.equals("-"))
            return false;
        try {
            double score = Double.parseDouble(lowestScoreStr.replace(",", "."));
            return userMerit >= score;
        } catch (NumberFormatException e) {
            log.warn("Kunde inte tolka antagningspoäng: '{}'", lowestScoreStr);
            return false;
        }
    }

    private boolean hasOnlyAsteriskScores(ProgramResponse.Program program) {
        String score1 = getLowestScoreBI_BII(program.getUrval1());
        String score2 = getLowestScoreBI_BII(program.getUrval2());
        return "*".equals(score1) && "*".equals(score2);
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

    private String getAntagningsUrl(String programnamn, String ort) {
        if (programnamn == null || ort == null)
            return null;

        try {
            String query = URLEncoder.encode(programnamn + " " + ort, StandardCharsets.UTF_8);
            return "https://www.antagning.se/se/search?&freeText=" + query;
        } catch (Exception e) {
            log.warn("Kunde inte bygga antagnings-URL för {} i {}: {}", programnamn, ort, e.getMessage());
            return null;
        }
    }
}
