package com.stegvis_api.stegvis_api.integration.openai.service;

import com.stegvis_api.stegvis_api.integration.openai.client.OpenAiHttpClient;
import com.stegvis_api.stegvis_api.integration.openai.model.AiChatRequest;
import com.stegvis_api.stegvis_api.integration.openai.model.AiChatResponse;
import com.stegvis_api.stegvis_api.integration.openai.model.AiMessage;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.SubjectCourseResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.model.CourseInfo;
import com.stegvis_api.stegvis_api.integration.skolverket.model.SubjectInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class OpenAiNoteService {

    @Value("${stegvis.openai.role.notes}")
    private String noteSystemPrompt;

    private final OpenAiHttpClient openAiHttpClient;

    private AiChatRequest buildNoteRequest(String model, String noteContent, SubjectCourseResponse subjectCourse,
            boolean stream) {
        SubjectInfo subject = subjectCourse.getSubject();
        CourseInfo course = subjectCourse.getCourse();

        String systemPrompt = noteSystemPrompt + "\n\n" +
                "Använd följande ämnesinformation när du fördjupar anteckningen:\n" +
                "Ämne: " + subject.getName() + "\n" +
                "Beskrivning: " + subject.getDescription() + "\n" +
                "Syfte: " + subject.getPurpose() + "\n\n" +
                "Fokusera på kursen:\n" +
                "Kurs: " + course.getName() + "\n" +
                "Beskrivning: " + course.getDescription() + "\n" +
                "Centralt innehåll: " + course.getCentralContent().getText() + "\n" +
                "Betygskriterier: " + course.getKnowledgeRequirements().stream()
                        .map(k -> k.getGradeStep() + ": " + k.getText())
                        .reduce((a, b) -> a + "\n" + b).orElse("");

        AiChatRequest request = AiChatRequest.builder()
                .model(model)
                .messages(List.of(
                        new AiMessage("system", systemPrompt),
                        new AiMessage("user", noteContent)))
                .n(1)

                .build();

        return request;
    }

    public String generateOptimizedNote(String noteContent, SubjectCourseResponse subjectCourse) {
        AiChatRequest request = buildNoteRequest("gpt-4o-mini", noteContent, subjectCourse, false);
        AiChatResponse response = openAiHttpClient.createChatCompletion(request);
        return response.getChoices().get(0).getMessage().getContent();
    }
}