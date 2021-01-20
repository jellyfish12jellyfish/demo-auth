package com.example.demo.service.implementation;
/*
 * Date: 12/28/20
 * Time: 9:24 AM
 * */

import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    // DI
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question findById(Long id) {
        log.info(">>> Find user by id: {}", id);
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question id not found: " + id));
    }

    @Override
    public Page<Question> findAllPageable(Long id, Pageable pageable) {
        log.info(">>> Get pageable questions");
        return questionRepository.findAllByThemeId(id, pageable);
    }

    @Override
    public List<Question> findAll() {
        List<Question> questionList = questionRepository.findAll();
        log.info(">>> Get all questions: {}", questionList.size());
        return questionList;
    }

    @Override
    public void deleteById(Long questionId) {
        log.info(">>> Delete question by id: {}", questionId);
        questionRepository.deleteById(questionId);
    }

    @Override
    public void createOrUpdate(Question question, Principal principal) {

        // если автор вопроса не найден, значит, это, скорее всего, новый вопрос
        if (question.getUser() == null) {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            question.setUser(user);
        }

        questionRepository.save(question);
    }

}
