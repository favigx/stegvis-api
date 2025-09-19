package com.stegvis_api.stegvis_api.integration.openai.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiChatRequest {

    private String model;
    private List<AiMessage> messages;
    private int n;

    public AiChatRequest(String model, String systemPrompt, String userPrompt, int n) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new AiMessage("system", systemPrompt));
        this.messages.add(new AiMessage("user", userPrompt));
        this.n = n;
    }
}