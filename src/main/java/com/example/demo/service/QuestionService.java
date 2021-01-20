package com.example.demo.service;

import com.example.demo.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface QuestionService {

    Question findById(Long id);

    Page<Question> findAllPageable(Long id, Pageable pageable);

    List<Question> findAll();

    void deleteById(Long questionId);

    void createOrUpdate(Question question, Principal principal);
}
