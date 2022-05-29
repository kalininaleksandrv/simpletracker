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
        return developerRepository.findById(id);
    }

    @Override
    public Developer save(Developer developer) {
        if(developer.getId()!=null){
            throw new DeveloperException("for new developers id must be null");
        }
        return developerRepository.save(developer);
    }

    @Override
    public Optional<Developer> update(Developer developer) {
        if(developer.getId() == null){
            throw new DeveloperException("for update, developer must contain id");
        }
        Optional<Developer> developerFromDb = developerRepository.findById(developer.getId());
        if(developerFromDb.isPresent()){
            Developer local = developerFromDb.get();
            local.setName(developer.getName());
            return Optional.of(developerRepository.save(local));
        }
        return developerFromDb;
    }

    @Override
    public void delete(Long id) {
        Optional<Developer> developerFromDb = developerRepository.findById(id);
        developerRepository.delete(developerFromDb
                .orElseThrow(() -> new DeveloperException("unable to delete developer - not found in db")));
    }
}
