package com.github.kalininaleksandrv.simpletracker.repository;

import com.github.kalininaleksandrv.simpletracker.model.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionPlanStorageImpl implements SessionPlanStorage {

    private Plan plan;

    @Override
    public Plan savePlan(Plan plan) {
        this.plan = plan;
        return plan;
    }

    @Override
    public Plan getOrCreatePlan() {
        if (plan == null) {
            return new Plan();
        }
        return plan;
    }

    @Override
    public Plan invalidatePlan() {
        plan = new Plan();
        return plan;
    }
}
