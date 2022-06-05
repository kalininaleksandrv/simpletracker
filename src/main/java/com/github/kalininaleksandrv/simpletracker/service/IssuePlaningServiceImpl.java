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
    private final DeveloperRepository developerRepository;
    private final SessionPlanStorage sessionPlanStorage;

    private Map<Developer, Integer> hoursTable;

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

        Set<Story> sortedStories = unplannedStories
                .stream()
                .sorted(Comparator.comparing(Story::getPoints))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return sessionPlanStorage.savePlan(calculateWeeklyPlan(plan, 1, sortedStories));
    }

    private Plan calculateWeeklyPlan(Plan plan, int week, Set<Story> stories) {
        initOrRefreshHourTable();
        for (Story s : stories) {
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

    private Set<Story> getAndFilterByStatus(StoryStatus status) {
        return issueBaseRepository
                .findAllByIssueType(IssueType.STORY)
                .stream()
                .map(i -> (Story) i)
                .filter(i -> i.getStoryStatus()!=null)
                .filter(i -> i.getStoryStatus().equals(status))
                .collect(Collectors.toSet());
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
