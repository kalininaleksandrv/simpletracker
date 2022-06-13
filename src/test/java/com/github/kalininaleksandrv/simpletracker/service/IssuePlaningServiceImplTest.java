package com.github.kalininaleksandrv.simpletracker.service;

import com.github.kalininaleksandrv.simpletracker.model.*;
import com.github.kalininaleksandrv.simpletracker.repository.DeveloperRepository;
import com.github.kalininaleksandrv.simpletracker.repository.IssueBaseRepository;
import com.github.kalininaleksandrv.simpletracker.repository.SessionPlanStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IssuePlaningServiceImplTest {

    @Autowired
    private IssuePlaningServiceImpl issuePlaningService;

    @MockBean
    private IssueBaseRepository issueBaseRepository;
    @MockBean
    private DeveloperRepository developerRepository;
    @MockBean
    private SessionPlanStorage sessionPlanStorage;

    @Test
    void calculatePlan() {

        Story first = new Story();
        first.setTitle("first");
        first.setStoryStatus(StoryStatus.NEW);
        first.setPoints(9);
        Story second = new Story();
        second.setTitle("second");
        second.setStoryStatus(StoryStatus.NEW);
        second.setPoints(1);
        Story third = new Story();
        third.setTitle("third");
        third.setStoryStatus(StoryStatus.NEW);
        third.setPoints(9);
        Developer developer1 = new Developer();
        developer1.setId(1L);
        developer1.setName("Developer1");
        List<Developer> listOfDev = new ArrayList<>();
        listOfDev.add(developer1);
        Set<Issue> stories = new HashSet<>();
        stories.add(first);
        stories.add(second);
        stories.add(third);

        when(developerRepository.findAll()).thenReturn(listOfDev);
        when(issueBaseRepository.findAllByIssueType(any())).thenReturn(stories);
        when(sessionPlanStorage.getOrCreatePlan()).thenReturn(new Plan());

        Plan plan = issuePlaningService.calculatePlan();

        assertEquals(3, plan.getAllStoriesFromPlan().size());
        assertEquals(2, plan.getNumOfWeek());
        assertEquals(2, plan.getPlanTable().get("Week 1").size());
        assertEquals(1, plan.getPlanTable().get("Week 2").size());
        assertEquals(10, plan.getAllocatedHours().get("Week 1"));
        assertEquals(9, plan.getAllocatedHours().get("Week 2"));
    }
}