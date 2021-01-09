package com.example.demo.repository;

import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import com.example.demo.entity.UserQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {

    boolean existsByQuestionAndUser(Question question, User user);

    Optional<UserQuestion> findByUserAndQuestion(User user, Question question);

    List<UserQuestion> findAllByUserIdOrderByLastViewAtDesc(Long userId, Pageable pageable);

}
