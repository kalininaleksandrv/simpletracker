package com.github.kalininaleksandrv.simpletracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Plan {
    @Getter
    @Setter
    private int numOfWeek;
    private final Map<Integer, Set<Story>> planTable;
    private Map<String, Integer> allocatedHours;

    public Plan() {
        this.planTable = new HashMap<>();
        this.numOfWeek = 1;
    }

    public void addStoryToPlan(Integer currNumberOfWeek, Story story) {
        numOfWeek = currNumberOfWeek;
        Set<Story> planToCurrentWeek = planTable.computeIfAbsent(currNumberOfWeek, i -> new HashSet<>());
        planToCurrentWeek.add(story);
        planTable.put(currNumberOfWeek, planToCurrentWeek);
    }

    @JsonIgnore
    public Set<Story> getAllStoriesFromPlan() {
        return planTable.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @JsonIgnore
    public int getNumberOfPlanedStories() {
        return planTable.size();
    }

    public Map<String, Set<Story>> getPlanTable() {
        return planTable
                .entrySet()
                .stream()
                .collect(Collectors.toMap(i -> "Week " + i.getKey().toString(), Map.Entry::getValue));
    }

    public Map<String, Integer> getAllocatedHours() {
        return planTable
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> "Week " + e.getKey().toString(), e -> e.getValue()
                        .stream()
                        .map(Story::getPoints)
                        .reduce(Integer::sum)
                        .orElse(0)));
    }
}
