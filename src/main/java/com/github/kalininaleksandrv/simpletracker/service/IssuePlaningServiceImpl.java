package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.exception.IssueProcessingException;
import com.github.kalininaleksandrv.simpletracker.model.*;
import com.github.kalininaleksandrv.simpletracker.repository.DeveloperRepository;
import com.github.kalininaleksandrv.simpletracker.repository.IssueBaseRepository;
import com.github.kalininaleksandrv.simpletracker.repository.SessionPlanStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssuePlaningServiceImpl implements IssuePlaningService {

    private final IssueBaseRepository issueBaseRepository;
    private final SessionPlanStorage sessionPlanStorage;

    private final WeeklyPlanCalculator weeklyPlanCalculator;

    @Override
    public Plan calculatePlan() {
        Set<Story> unplannedStories = getAndFilterByStatus(StoryStatus.NEW);
        Plan plan = sessionPlanStorage.getOrCreatePlan();
        Set<Story> allStoriesFromPlan = plan.getAllStoriesFromPlan();
        if (allStoriesFromPlan.size() != 0 && unplannedStories.size() != 0) {
            /*
              since we have additional storage we must perform total re-planing
              consider that not only set of stories but number of developers may change
             */
            for (Story s : allStoriesFromPlan) {
                s.unplane();
            }
            issueBaseRepository.saveAll(allStoriesFromPlan);
            unplannedStories.addAll(allStoriesFromPlan);
            plan = sessionPlanStorage.invalidatePlan();
        }
        Plan calculated = weeklyPlanCalculator.calculateWeeklyPlan(plan, 1, unplannedStories);
        return sessionPlanStorage.savePlan(calculated);
    }

    private Set<Story> getAndFilterByStatus(StoryStatus status) {
        return issueBaseRepository
                .findAllByIssueType(IssueType.STORY)
                .stream()
                .map(i -> (Story) i)
                .filter(i -> i.getStoryStatus()!=null)
                .filter(i -> i.getStoryStatus().equals(status))
                .collect(Collectors.toSet());
    }
}
