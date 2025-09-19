package com.stegvis_api.stegvis_api.integration.openai.model;

import java.util.List;

public class AiChatResponse {

    private List<Choice> choices;

    public AiChatResponse() {

    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public static class Choice {
        private AiMessage message;

        public Choice() {

        }

        public AiMessage getMessage() {
            return message;
        }

        public void setMessage(AiMessage message) {
            this.message = message;
        }
    }
}