package com.github.kalininaleksandrv.simpletracker.repository;

import com.github.kalininaleksandrv.simpletracker.model.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface IssueBaseRepository extends PagingAndSortingRepository<Issue, Long> {

    Optional<Issue> findByIssueId(String issueId);

    @EntityGraph(
            type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {
                    "developer.name"
            }
    )
    Page<Issue> findAll(Pageable pageable);

    void delete(Issue issue);
}
