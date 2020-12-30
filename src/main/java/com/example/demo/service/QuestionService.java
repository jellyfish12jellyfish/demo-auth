package com.example.demo.service;
/*
 * Date: 12/28/20
 * Time: 9:24 AM
 * */

import com.example.demo.entity.Question;
import com.example.demo.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    public Question findById(Long id) {
        questionRepository.setLastViewTime(id);

        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question id not found: " + id));
    }

}
