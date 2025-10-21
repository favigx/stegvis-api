package com.stegvis_api.stegvis_api.integration.openai.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.stegvis_api.stegvis_api.integration.openai.model.AiChatRequest;
import com.stegvis_api.stegvis_api.integration.openai.model.AiChatResponse;

@Component
public class OpenAiWebClientAdapter implements OpenAiHttpClient {

    private final WebClient webClient;

    public OpenAiWebClientAdapter(WebClient openAiWebClient) {
        this.webClient = openAiWebClient;
    }

    @Override
    public AiChatResponse createChatCompletion(AiChatRequest request) {
        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AiChatResponse.class)
                .block();
    }
}
