package com.github.kalininaleksandrv.simpletracker.repository;

import com.github.kalininaleksandrv.simpletracker.model.Plan;

public interface SessionPlanStorage {

    Plan savePlan(Plan plan);

    Plan getOrCreatePlan();

    Plan invalidatePlan();
}
