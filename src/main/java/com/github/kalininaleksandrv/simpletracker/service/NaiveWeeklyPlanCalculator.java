package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.exception.IssueProcessingException;
import com.github.kalininaleksandrv.simpletracker.model.Developer;
import com.github.kalininaleksandrv.simpletracker.model.Plan;
import com.github.kalininaleksandrv.simpletracker.model.Story;
import com.github.kalininaleksandrv.simpletracker.model.StoryStatus;
import com.github.kalininaleksandrv.simpletracker.repository.DeveloperRepository;
import com.github.kalininaleksandrv.simpletracker.repository.IssueBaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/*
simplest naive approach - sort stories by points and start allocating from min,
so chances to utilize more time from each developer will increase
 */
@Component
@RequiredArgsConstructor
public class NaiveWeeklyPlanCalculator implements WeeklyPlanCalculator {

    private final DeveloperRepository developerRepository;
    private final IssueBaseRepository issueBaseRepository;
    private Map<Developer, Integer> hoursTable;

    @Override
    public Plan calculateWeeklyPlan(Plan plan, int week, Set<Story> stories) {


        Set<Story> sortedStories = stories
                .stream()
                .sorted(Comparator.comparing(Story::getPoints))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        initOrRefreshHourTable();
        for (Story s : sortedStories) {
            if (s.getStoryStatus() == StoryStatus.NEW) {
                for (Developer d : hoursTable.keySet()) {
                    Integer remainingHours = s.plane(d, hoursTable.get(d));
                    if (remainingHours > -1) {
                        hoursTable.put(d, remainingHours);
                        plan.addStoryToPlan(week, s);
                        issueBaseRepository.save(s);
                        break;
                    }
                }
            } else {
                break;
            }
        }
        Set<Story> remainingStories = stories
                .stream()
                .filter(i -> i.getStoryStatus().equals(StoryStatus.NEW))
                .collect(Collectors.toSet());

        if (remainingStories.size() < 1) {
            return plan;
        }
        return calculateWeeklyPlan(plan, week + 1, remainingStories);
    }

    private void initOrRefreshHourTable() {
        List<Developer> developers;
        if(hoursTable ==null){
            developers = developerRepository.findAll();
            if (developers.size() < 1) {
                throw new IssueProcessingException("no developers, unable to allocate issues");
            }
            hoursTable = new HashMap<>();
        } else {
            developers = new ArrayList<>(hoursTable.keySet());
        }
        for (Developer d : developers) {
            hoursTable.put(d, 10);
        }
    }
}
