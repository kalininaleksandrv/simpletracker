package com.github.kalininaleksandrv.simpletracker.repository;

import com.github.kalininaleksandrv.simpletracker.model.Issue;
import com.github.kalininaleksandrv.simpletracker.model.IssueType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.Set;

public interface IssueBaseRepository extends PagingAndSortingRepository<Issue, Long> {

    Optional<Issue> findByIssueId(String issueId);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "developer.name"
            }
    )
    Page<Issue> findAll(Pageable pageable);

    Set<Issue> findAll();

    void delete(Issue issue);

    Set<Issue> findAllByIssueType(IssueType story);
}
