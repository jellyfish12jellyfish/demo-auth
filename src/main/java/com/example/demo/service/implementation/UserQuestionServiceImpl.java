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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserQuestionServiceImpl implements UserQuestionService {

    private final UserQuestionRepository userQuestionRepository;

    public UserQuestionServiceImpl(UserQuestionRepository userQuestionRepository) {
        this.userQuestionRepository = userQuestionRepository;
    }

    @Override
    public void save(UserQuestion userQuestion) {
        userQuestion.setLastViewAt(new Date());
        userQuestionRepository.save(userQuestion);
        log.info(">>> Save UserQuestioin object: {}", userQuestion);
    }

    @Override
    public void setViewTime(User user, Question question) {

        log.info(">>> Get UserQuestion object by User and Question");
        UserQuestion userQuestion = userQuestionRepository
                .findByUserAndQuestion(user, question)
                .orElse(null);

        try {

            if (userQuestion == null) {
                save(new UserQuestion(user, question));
            } else {
                save(userQuestion);
            }

        } catch (Exception e) {
            log.error(">>> Error in UserQuestionServiceImpl: ");
            e.printStackTrace();
        }

    }

    @Override
    public List<UserQuestion> findAllByUserId(Long userId) {
        log.info(">>> Get 10 UserQuestions objects");
        return userQuestionRepository
                .findAllByUserIdOrderByLastViewAtDesc(userId, PageRequest.of(0, 10));
    }
}
