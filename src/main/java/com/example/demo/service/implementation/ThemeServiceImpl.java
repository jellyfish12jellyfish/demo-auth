package com.example.demo.service.implementation;
/*
 * Date: 12/30/20
 * Time: 11:58 AM
 * */

import com.example.demo.entity.Theme;
import com.example.demo.repository.ThemeRepository;
import com.example.demo.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeServiceImpl(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

}
