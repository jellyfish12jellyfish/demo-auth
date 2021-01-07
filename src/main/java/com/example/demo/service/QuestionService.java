package com.example.demo.service;

import com.example.demo.entity.Question;

import java.util.List;

public interface QuestionService {

    Question findById(Long id);

    List<Question> getThemeQuestions(Long id);
}
