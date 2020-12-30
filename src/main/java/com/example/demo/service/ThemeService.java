package com.example.demo.service;
/*
 * Date: 12/30/20
 * Time: 11:58 AM
 * */

import com.example.demo.entity.Theme;
import com.example.demo.repository.ThemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

}
