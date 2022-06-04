package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.exception.DeveloperException;
import com.github.kalininaleksandrv.simpletracker.exception.IssueProcessingException;
import com.github.kalininaleksandrv.simpletracker.model.*;
import com.github.kalininaleksandrv.simpletracker.repository.IssueBaseRepository;
import com.github.kalininaleksandrv.simpletracker.utils.IssueValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class IssueServiceImpl implements IssueService {

    private final IssueBaseRepository issueBaseRepository;

    @Override
    public Page<Issue> getAllIssues(int page, int size) {
        return issueBaseRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Optional<Issue> getIssueByUid(UUID uuid) {
        return issueBaseRepository.findByIssueId(uuid.toString());
    }

    @Override
    public Issue save(Issue issue) {
        if (issue.getIssueId() != null) {
            throw new IssueProcessingException("new issues must not contains issueId");
        }
        IssueValidator.validate(issue);
        issue.setIssueId(UUID.randomUUID().toString());
        issue.setDateTime(LocalDateTime.now());
        issue.setStatusToNew();
        return issueBaseRepository.save(issue);
    }

    @Override
    public Issue update(Issue issue) {
        if (issue.getIssueId() == null || issue.getIssueType() == null) {
            throw new IssueProcessingException("for update, issue must contain id and TYPE");
        }
        Optional<Issue> issueFromDb = issueBaseRepository.findByIssueId(issue.getIssueId());

        if (issueFromDb.isPresent()) {
            IssueValidator.validateForUpdate(issueFromDb.get(), issue);

            issueFromDb.get().setTitle(issue.getTitle());
            issueFromDb.get().setDescription(issue.getDescription());
            issueFromDb.get().setDeveloper(issue.getDeveloper());

            if (issue.getIssueType() == IssueType.STORY) {
                Story fromDb = (Story) issueFromDb.get();
                fromDb.setStoryStatus(((Story) issue).getStoryStatus());
                return issueBaseRepository.save(fromDb);
            }
            if (issue.getIssueType() == IssueType.BUG) {
                Bug fromDb = (Bug) issueFromDb.get();
                fromDb.setBugStatus(((Bug) issue).getBugStatus());
                fromDb.setBugPriority(((Bug) issue).getBugPriority());
                return issueBaseRepository.save(fromDb);
            }
        }
        throw new IssueProcessingException("unable to uprate issue");
    }

    @Override
    public void delete(UUID id) {
        Optional<Issue> issueFromDb = issueBaseRepository.findByIssueId(id.toString());
        issueBaseRepository.delete(issueFromDb
                .orElseThrow(() -> new DeveloperException("unable to delete issue - not found in db")));
    }
}
