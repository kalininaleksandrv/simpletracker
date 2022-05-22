package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.exception.IssueProcessingException;
import com.github.kalininaleksandrv.simpletracker.model.Bug;
import com.github.kalininaleksandrv.simpletracker.model.Issue;
import com.github.kalininaleksandrv.simpletracker.model.IssueType;
import com.github.kalininaleksandrv.simpletracker.model.Story;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class IssueServiceImpl implements IssueService {
    @Override
    public Page<Issue> getAllIssues(int page, int size) {
        return null;
    }

    @Override
    public Optional<Issue> getIssueByUid(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Issue save(Issue issue) {
        if(issue.getIssueId() != null){
            throw new IssueProcessingException("new issues must not contains issueId");
        }
        if(issue.getIssueType() == IssueType.STORY && issue.getDeveloper()!=null){
            throw new IssueProcessingException("story must not be assigned due creation");
        }
        if(issue.getIssueType().equals(IssueType.STORY)){
            Story story = (Story) issue;
            if(story.getPoints()<=0){
               throw new IssueProcessingException("story must be estimate before creation");
            }
        }
        if(issue.getIssueType().equals(IssueType.BUG)){
            Bug bug = (Bug) issue;
            if(bug.getPriority()==null){
                throw new IssueProcessingException("bug must be prioritized before creation");
            }
        }
        issue.setIssueId(UUID.randomUUID().toString());
        issue.setDateTime(LocalDateTime.now());
        return issue;
    }
}
