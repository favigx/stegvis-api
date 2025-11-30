package com.stegvis_api.stegvis_api.integration.openai.client;

import com.stegvis_api.stegvis_api.integration.openai.model.AiChatRequest;
import com.stegvis_api.stegvis_api.integration.openai.model.AiChatResponse;

public interface OpenAiHttpClient {
    AiChatResponse createChatCompletion(AiChatRequest request);
}