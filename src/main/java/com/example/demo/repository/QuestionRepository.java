package com.example.demo.repository;

import com.example.demo.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Modifying
    @Transactional
    @Query("update Question q set q.lastViewAt = current_timestamp where q.id=:id")
    void setLastViewTime(@Param("id") Long id);
}
