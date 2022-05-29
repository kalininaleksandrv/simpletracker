package com.github.kalininaleksandrv.simpletracker.controller;

import com.github.kalininaleksandrv.simpletracker.model.Developer;
import com.github.kalininaleksandrv.simpletracker.model.Issue;
import com.github.kalininaleksandrv.simpletracker.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
@Slf4j
public class DeveloperController {

    private final DeveloperService developerService;

    @PostMapping(path = "developer")
    public ResponseEntity<Developer> newIssue(@RequestBody Developer developer) {
        var savedDeveloper = developerService.save(developer);
        return new ResponseEntity<>(savedDeveloper, HttpStatus.OK);
    }
}