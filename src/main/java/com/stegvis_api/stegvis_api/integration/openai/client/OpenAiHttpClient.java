package com.stegvis_api.stegvis_api.integration.openai.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.stegvis_api.stegvis_api.integration.openai.model.AiChatRequest;
import com.stegvis_api.stegvis_api.integration.openai.model.AiChatResponse;

public interface OpenAiHttpClient {

    @PostExchange("/chat/completions")
    AiChatResponse createChatCompletion(@RequestBody AiChatRequest request);
}