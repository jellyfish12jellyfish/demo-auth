package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User where username=:name")
    User findByUsername(@Param("name") String username);

    @Modifying
    @Transactional
    @Query("update User u set u.createdAt = current_timestamp where u.username=:name")
    void setLastLoginTime(@Param("name") String username);

}
