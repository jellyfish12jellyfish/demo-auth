package com.example.demo.service;

import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import com.example.demo.entity.UserQuestion;

import java.util.List;

public interface UserQuestionService {

    void save(UserQuestion userQuestion);

    void setViewTime(User user, Question question);

    List<UserQuestion> findAllByUserId(Long userId);
}
