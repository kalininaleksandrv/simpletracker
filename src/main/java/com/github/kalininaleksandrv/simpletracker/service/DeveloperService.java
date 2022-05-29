package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.model.Developer;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DeveloperService {
    List<Developer> getAllDevelopers();

    Optional<Developer> getDeveloperById(long id);

    Developer save(Developer developer);
}
