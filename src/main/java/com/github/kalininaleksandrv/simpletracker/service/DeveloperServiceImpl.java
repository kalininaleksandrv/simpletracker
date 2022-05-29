package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.exception.DeveloperException;
import com.github.kalininaleksandrv.simpletracker.model.Developer;
import com.github.kalininaleksandrv.simpletracker.repository.DeveloperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    @Override
    public List<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }

    @Override
    public Optional<Developer> getDeveloperById(long id) {
        return Optional.empty();
    }

    @Override
    public Developer save(Developer developer) {
        if(developer.getId()!=null){
            throw new DeveloperException("for new developers id must be null");
        }
        return developerRepository.save(developer);
    }
}
