package com.stegvis_api.stegvis_api.integration.openai.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.integration.openai.client.OpenAiHttpClient;
import com.stegvis_api.stegvis_api.integration.openai.model.AiChatRequest;
import com.stegvis_api.stegvis_api.integration.openai.model.AiChatResponse;
import com.stegvis_api.stegvis_api.integration.openai.model.AiMessage;
import com.stegvis_api.stegvis_api.notes.model.Note;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class OpenAiNoteQuizService {

  private final OpenAiHttpClient openAiHttpClient;

  private AiChatRequest buildNoteQuizRequest(String model, Note note) {
    String systemPrompt = """
        Du är en expert på att skapa pedagogiska quiz på svenska.

        Skapa ett flervalsquiz baserat på följande anteckning.

        Krav:
        - Quizet ska ha ett tydligt namn baserat på innehållet i anteckningen.
        - Använd **inte** ämnets eller kursens namn i quiz-namnet (t.ex. "Engelska 5").
        - Quiz-namnet ska vara kort, beskrivande och avslutas med ordet "quiz".
        - Svara ENDAST i strikt JSON enligt formatet nedan (ingen extra text före eller efter).

        JSON-format:
        {
          "quizName": "Quiznamn utan ämne",
          "questions": [
            {
              "question": "Fråga",
              "options": [
                { "optionText": "Svar A", "points": 0, "isCorrect": false },
                { "optionText": "Svar B", "points": 1, "isCorrect": true },
                { "optionText": "Svar C", "points": 0, "isCorrect": false },
                { "optionText": "Svar D", "points": 0, "isCorrect": false }
              ]
            }
          ]
        }

        Anteckning:
        Ämne: %s
        Innehåll: %s
        """.formatted(note.getSubject(), note.getNote());

    return AiChatRequest.builder()
        .model(model)
        .messages(List.of(
            new AiMessage("system", systemPrompt),
            new AiMessage("user", "Skapa quiz enligt instruktionerna ovan.")))
        .n(1)
        .build();
  }

  public String generateNoteQuiz(Note note) {
    AiChatRequest request = buildNoteQuizRequest("gpt-4o-mini", note);
    AiChatResponse response = openAiHttpClient.createChatCompletion(request);
    String aiContent = response.getChoices().get(0).getMessage().getContent();

    log.info("AI Quiz Content:\n{}", aiContent);

    return aiContent;
  }
}
