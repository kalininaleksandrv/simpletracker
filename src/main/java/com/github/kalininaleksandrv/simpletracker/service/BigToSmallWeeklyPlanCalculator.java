package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.model.Developer;
import com.github.kalininaleksandrv.simpletracker.model.Plan;
import com.github.kalininaleksandrv.simpletracker.model.Story;
import com.github.kalininaleksandrv.simpletracker.model.StoryStatus;
import com.github.kalininaleksandrv.simpletracker.repository.IssueBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BigToSmallWeeklyPlanCalculator implements WeeklyPlanCalculator {

    private static final Integer INIT_HOUR = 10;
    private final IssueBaseRepository issueBaseRepository;
    private Map<Developer, Integer> hoursTable;

    @Override
    public Plan calculateWeeklyPlan(Plan plan, int week, Set<Story> stories, Map<Developer, Integer> hoursTable) {

        refreshHourTable(hoursTable);

        Set<Story> sortedStories = stories
                .stream()
                .sorted(Comparator.comparing(Story::getPoints).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Developer d : hoursTable.keySet()) {
            for (Story s : sortedStories) {
                Integer freeHours = hoursTable.get(d);
                if (s.getStoryStatus() == StoryStatus.NEW && s.getPoints() <= freeHours) {
                    s.plane(d);
                    freeHours -= s.getPoints();
                    hoursTable.put(d, freeHours);
                    plan.addStoryToPlan(week, s);
                }
            }
        }

        Set<Story> unplanned = sortedStories
                .stream()
                .filter(i -> i.getStoryStatus() == StoryStatus.NEW)
                .collect(Collectors.toSet());

        if (unplanned.size() > 0) {
            calculateWeeklyPlan(plan, week + 1, unplanned, hoursTable);
        }
        return plan;
    }

    private void refreshHourTable(Map<Developer, Integer> hoursTable) {
        hoursTable.replaceAll((k, v) -> v = INIT_HOUR);
    }
}
