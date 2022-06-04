package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.model.Plan;
import org.springframework.stereotype.Service;

@Service
public class IssuePlaningServiceImpl implements IssuePlaningService {
    @Override
    public Plan calculatePlan() {
        return new Plan();
    }
}
