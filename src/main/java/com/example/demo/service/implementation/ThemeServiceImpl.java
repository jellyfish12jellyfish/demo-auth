package com.example.demo.service.implementation;
/*
 * Date: 12/30/20
 * Time: 11:58 AM
 * */

import com.example.demo.entity.Theme;
import com.example.demo.repository.ThemeRepository;
import com.example.demo.service.ThemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeServiceImpl(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public List<Theme> getThemes() {
        List<Theme> themes = themeRepository.findAll();
        log.info(">>> Get all themes: {}", themes.size());
        return themes;
    }

    @Override
    public void deleteThemeById(Long themeId) {
        log.info(">>> Delete theme by id: {}", themeId);
        themeRepository.deleteById(themeId);
    }

    @Override
    public Theme getThemeById(Long themeId) {
        return themeRepository.findById(themeId).orElseThrow(() -> new RuntimeException("Theme not found: " + themeId));
    }

    @Override
    public void save(Theme theme) {
        themeRepository.save(theme);
    }

}
