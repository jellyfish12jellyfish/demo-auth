package com.example.demo.repository;

import com.example.demo.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

//    @Modifying
//    @Transactional
//    @Query("update Question q set q.lastViewAt = current_timestamp where q.id=:id")
//    void setLastViewTime(@Param("id") Long id);
}
