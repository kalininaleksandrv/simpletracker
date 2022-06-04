package com.github.kalininaleksandrv.simpletracker.controller;

import com.github.kalininaleksandrv.simpletracker.model.Issue;
import com.github.kalininaleksandrv.simpletracker.model.Plan;
import com.github.kalininaleksandrv.simpletracker.service.IssuePlaningService;
import com.github.kalininaleksandrv.simpletracker.service.IssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
@Slf4j
public class IssueController {

    private final IssueService issueService;
    private final IssuePlaningService issuePlaningService;

    @GetMapping(path = "issues/{id}")
    public ResponseEntity<Issue> getIssueById(@PathVariable UUID id) {

        Optional<Issue> issue = issueService.getIssueByUid(id);
        return issue.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
    }

    @PostMapping(path = "issue")
    public ResponseEntity<Issue> save(@RequestBody Issue issue) {
        var savedIssue = issueService.save(issue);
        return new ResponseEntity<>(savedIssue, HttpStatus.OK);
    }

    @PutMapping(path = "issue")
    public ResponseEntity<Issue> update(@RequestBody Issue issue) {
        var issueFromDb = issueService.update(issue);
        return new ResponseEntity<>(issueFromDb, HttpStatus.OK);
    }

    @DeleteMapping(path = "issue/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        issueService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "issues")
    public ResponseEntity<Page<Issue>> findAll(@RequestParam int page, @RequestParam(required = false, defaultValue = "100") int size) {
        if (page < 0) throw new IllegalArgumentException("page must be >= 0");
        Page<Issue> user = issueService.getAllIssues(page, size);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/plan")
    public ResponseEntity<Plan> plan() {

        Plan plan = issuePlaningService.calculatePlan();
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }
}
