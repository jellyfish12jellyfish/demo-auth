package com.example.demo.repository;
/*
 * Date: 12/30/20
 * Time: 11:57 AM
 * */

import com.example.demo.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
