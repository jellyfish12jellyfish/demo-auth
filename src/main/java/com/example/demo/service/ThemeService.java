package com.example.demo.service;

import com.example.demo.entity.Theme;

import java.util.List;

public interface ThemeService {

    List<Theme> findAll();

    void deleteById(Long themeId);

    Theme getThemeById(Long themeId);

    void save(Theme theme);
}
