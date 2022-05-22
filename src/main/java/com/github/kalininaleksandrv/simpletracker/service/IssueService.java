package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.model.Issue;
import org.springframework.data.domain.Page;

/**
 * interface to perform operations on Issue - Stories and bugs
 */
public interface IssueService {
    Page<Issue> getAllIssues(int page, int size);
}
