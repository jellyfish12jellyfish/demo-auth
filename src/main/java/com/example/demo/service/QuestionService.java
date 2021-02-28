package com.example.demo.service;

import com.example.demo.entity.Question;
import com.example.demo.entity.Theme;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {

    Question getQuestionById(Long id);

    Page<Question> getPageableQuestions(Long id, Pageable pageable);

    List<Question> getQuestions();

    void deleteQuestionById(Long questionId);

    void createOrUpdateQuestion(Question question, User userFromDb, Theme theme);

}
