package com.example.demo.service;

import com.example.demo.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionService {

    Question findById(Long id);

    Page<Question> findAllPageable(Long id, Pageable pageable);

}
