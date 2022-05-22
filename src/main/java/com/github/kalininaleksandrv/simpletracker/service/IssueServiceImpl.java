package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.model.Issue;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class IssueServiceImpl implements IssueService {
    @Override
    public Page<Issue> getAllIssues(int page, int size) {
        return null;
    }
}
