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

  /**
   * Bygger request till OpenAI med strikt JSON-prompt som instruerar AI:
   * - Skapa quiz baserat på anteckningen
   * - Markera exakt ett korrekt svar per fråga
   * - Svara endast med JSON
   */
  private AiChatRequest buildNoteQuizRequest(String model, Note note) {
    String systemPrompt = """
        Skapa ett flervalsquiz baserat på följande anteckning.
        Varje fråga ska ha exakt ett korrekt svar markerat med "isCorrect": true.
        Använd endast fakta som finns i anteckningen.
        Svara endast i strikt JSON enligt formatet nedan, utan någon annan text.

        Anteckning:
        Ämne: %s
        Innehåll: %s

        JSON-format:
        [
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
        """.formatted(note.getSubject(), note.getNote());

    return AiChatRequest.builder()
        .model(model)
        .messages(List.of(
            new AiMessage("system", systemPrompt),
            new AiMessage("user", "Skapa quiz enligt ovan instruktioner")))
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
