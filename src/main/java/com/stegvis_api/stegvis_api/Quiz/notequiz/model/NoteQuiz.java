package com.stegvis_api.stegvis_api.Quiz.notequiz.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "quizzes")
public class NoteQuiz {

    @Id
    private String id;

    private String userId;
    private String quizName;
    private String courseName;
    private List<Questions> questions;
}
