package com.github.kalininaleksandrv.simpletracker.repository;

import com.github.kalininaleksandrv.simpletracker.model.Plan;
import com.github.kalininaleksandrv.simpletracker.model.Story;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionPlanStorageImpl implements SessionPlanStorage {

    private Plan plan;
    private final IssueBaseRepository issueBaseRepository;

    @Override
    public void savePlan(Plan plan) {
        issueBaseRepository.saveAll(plan.getAllStoriesFromPlan());
        this.plan = plan;
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
        for (Story s : plan.getAllStoriesFromPlan()) {
            s.unplane();
        }
        issueBaseRepository.saveAll(plan.getAllStoriesFromPlan());
        plan = new Plan();
        return plan;
    }
}
