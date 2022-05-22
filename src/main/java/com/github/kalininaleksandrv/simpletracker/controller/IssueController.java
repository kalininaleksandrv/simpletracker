package com.github.kalininaleksandrv.simpletracker.controller;

import com.github.kalininaleksandrv.simpletracker.exception.IssueProcessingException;
import com.github.kalininaleksandrv.simpletracker.model.Issue;
import com.github.kalininaleksandrv.simpletracker.service.IssueServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
@Slf4j
public class IssueController {

    private final IssueServiceImpl issueService;

    @GetMapping(path = "issues")
    public ResponseEntity<Page<Issue>> getAllIssues(@PathVariable() int page,
                                                    @RequestParam(required = false, defaultValue = "100") int size) {
        if (page < 0) throw new IllegalArgumentException("page must be >= 0");
        log.debug("starting request in /api/v1/issues");
        Page<Issue> user = issueService.getAllIssues(page, size);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(path = "issue")
    public ResponseEntity<Issue> newIssue(@RequestBody Issue issue) {
        var savedIssue = issueService.save(issue);
        return new ResponseEntity<>(savedIssue, HttpStatus.OK);
    }
}
