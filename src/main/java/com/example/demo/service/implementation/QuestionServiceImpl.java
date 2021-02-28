package com.example.demo.service.implementation;
/*
 * Date: 12/28/20
 * Time: 9:24 AM
 * */

import com.example.demo.entity.Question;
import com.example.demo.entity.Theme;
import com.example.demo.entity.User;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {

    // DI
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question getQuestionById(Long id) {
        log.info(">>> Find user by id: {}", id);
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question id not found: " + id));
    }

    @Override
    public Page<Question> getPageableQuestions(Long id, Pageable pageable) {
        log.info(">>> Get pageable questions");
        return questionRepository.findAllByThemeId(id, pageable);
    }

    @Override
    public List<Question> getQuestions() {
        List<Question> questionList = questionRepository.findAll();
        log.info(">>> Get all questions: {}", questionList.size());
        return questionList;
    }

    @Override
    public void deleteQuestionById(Long questionId) {
        log.info(">>> Delete question by id: {}", questionId);
        questionRepository.deleteById(questionId);
    }

    @Override
    public void createOrUpdateQuestion(Question question, User userFromDb, Theme theme) {

        // если автор вопроса не найден, значит, это, скорее всего, новый вопрос
        if (question.getUser() == null) {
            question.setUser(userFromDb);
        }
        question.setUpdatedAt(new Date());
        question.setTheme(theme);
        questionRepository.save(question);
    }

}
