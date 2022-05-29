package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.model.Issue;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

/**
 * interface to perform operations on Issue - Stories and bugs
 */
public interface IssueService {
    Page<Issue> getAllIssues(int page, int size);

    Optional<Issue> getIssueByUid(UUID uuid);

    Issue save(Issue issue);

    Issue update(Issue issue);

    void delete(UUID id);
}
