package com.example.demo.service.implementation;
/*
 * Date: 1/9/21
 * Time: 8:38 AM
 * */

import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import com.example.demo.entity.UserQuestion;
import com.example.demo.repository.UserQuestionRepository;
import com.example.demo.service.UserQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserQuestionServiceImpl implements UserQuestionService {

    private static final Logger log = LoggerFactory.getLogger(UserQuestionServiceImpl.class.getName());

    private final UserQuestionRepository userQuestionRepository;

    public UserQuestionServiceImpl(UserQuestionRepository userQuestionRepository) {
        this.userQuestionRepository = userQuestionRepository;
    }

    @Override
    public void save(UserQuestion userQuestion) {
        userQuestion.setLastViewAt(new Date());
        userQuestionRepository.save(userQuestion);
    }

    @Override
    public UserQuestion findByUserAndQuestion(User user, Question question) {
        return userQuestionRepository.findByUserAndQuestion(user, question).orElse(null);
    }

    @Override
    public List<UserQuestion> findAllByUserId(Long userId) {
        return userQuestionRepository
                .findAllByUserIdOrderByLastViewAtDesc(userId, PageRequest.of(0, 10));
    }
}
