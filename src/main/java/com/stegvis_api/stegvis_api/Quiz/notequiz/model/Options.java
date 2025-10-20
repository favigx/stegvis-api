package com.stegvis_api.stegvis_api.Quiz.notequiz.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Options {
    private String optionText;
    private int points;

    @JsonProperty("isCorrect")
    private boolean correct;

}
