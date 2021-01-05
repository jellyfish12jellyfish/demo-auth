package com.example.demo.service.implementation;
/*
 * Date: 12/28/20
 * Time: 9:24 AM
 * */

import com.example.demo.entity.Question;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    // DI
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question findById(Long id) {
        log.info(">>> find user by id: {}", id);
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question id not found: " + id));
    }
}
