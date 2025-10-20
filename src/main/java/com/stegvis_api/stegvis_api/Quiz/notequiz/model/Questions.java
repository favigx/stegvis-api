package com.stegvis_api.stegvis_api.Quiz.notequiz.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Questions {

    private String question;
    private List<Options> options;
}
