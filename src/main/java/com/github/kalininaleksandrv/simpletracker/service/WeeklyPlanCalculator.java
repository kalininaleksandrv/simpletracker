package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.model.Developer;
import com.github.kalininaleksandrv.simpletracker.model.Plan;
import com.github.kalininaleksandrv.simpletracker.model.Story;

import java.util.Map;
import java.util.Set;

public interface WeeklyPlanCalculator {
    Plan calculateWeeklyPlan(Plan plan, int week, Set<Story> stories, Map<Developer, Integer> hoursMap);
}
