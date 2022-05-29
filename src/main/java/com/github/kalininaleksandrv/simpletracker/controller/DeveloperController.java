package com.github.kalininaleksandrv.simpletracker.controller;

import com.github.kalininaleksandrv.simpletracker.exception.DeveloperException;
import com.github.kalininaleksandrv.simpletracker.model.Developer;
import com.github.kalininaleksandrv.simpletracker.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
@Slf4j
public class DeveloperController {

    private final DeveloperService developerService;

    @GetMapping(path = "developers/{id}")
    public ResponseEntity<Developer> getById (@PathVariable Long id){
        var developer = developerService.getDeveloperById(id);
        return developer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
    }

    @PostMapping(path = "developer")
    public ResponseEntity<Developer> newDeveloper (@RequestBody Developer developer) {
        var savedDeveloper = developerService.save(developer);
        return new ResponseEntity<>(savedDeveloper, HttpStatus.OK);
    }

    @PutMapping(path = "developer")
    public ResponseEntity<Developer> update (@RequestBody Developer developer){
        var developerFromDb = developerService.update(developer);
        return developerFromDb.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
    }

    @DeleteMapping(path = "developers/{id}")
    public ResponseEntity<String> delete (@PathVariable Long id){
        developerService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "developers")
    public ResponseEntity<List<Developer>> findAll (){
        var developers = developerService.getAllDevelopers();
        if (developers.size() < 1) throw new DeveloperException("developer list is empty");
        return new ResponseEntity<>(developers, HttpStatus.OK);
    }
}