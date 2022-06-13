package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.exception.IssueProcessingException;
import com.github.kalininaleksandrv.simpletracker.model.*;
import com.github.kalininaleksandrv.simpletracker.repository.DeveloperRepository;
import com.github.kalininaleksandrv.simpletracker.repository.IssueBaseRepository;
import com.github.kalininaleksandrv.simpletracker.repository.SessionPlanStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssuePlaningServiceImpl implements IssuePlaningService {

    private final IssueBaseRepository issueBaseRepository;
    private final DeveloperRepository developerRepository;
    private final SessionPlanStorage sessionPlanStorage;
    private final WeeklyPlanCalculator weeklyPlanCalculator;

    @Override
    public Plan calculatePlan() {
        Map<Developer, Integer> hoursMap = initHourTable();
        Set<Story> unplannedStories = getAndFilterByStatus(StoryStatus.NEW);
        Plan plan = sessionPlanStorage.getOrCreatePlan();
        //if true - full re-planing must be performed
        if (plan.getNumberOfPlanedStories() != 0 && unplannedStories.size() != 0) {
            unplannedStories.addAll(plan.getAllStoriesFromPlan());
            plan = sessionPlanStorage.invalidatePlan();
        }
        Plan calculatedPlan = weeklyPlanCalculator.calculateWeeklyPlan(plan, 1, unplannedStories, hoursMap);
        sessionPlanStorage.savePlan(calculatedPlan);
        return calculatedPlan;
    }

    private Set<Story> getAndFilterByStatus(StoryStatus status) {
        return issueBaseRepository
                .findAllByIssueType(IssueType.STORY)
                .stream()
                .map(i -> (Story) i)
                .filter(i -> i.getStoryStatus() != null)
                .filter(i -> i.getStoryStatus().equals(status))
                .collect(Collectors.toSet());
    }

    private Map<Developer, Integer> initHourTable() {
        List<Developer> developers = developerRepository.findAll();
        if (developers.size() < 1) {
            throw new IssueProcessingException("no developers, unable to allocate issues");
        }
        Map<Developer, Integer> hoursTable = new HashMap<>();
        for (Developer d : developers) {
            hoursTable.put(d, 0);
        }
        return hoursTable;
    }
}
