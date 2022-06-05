package com.github.kalininaleksandrv.simpletracker.controller;

import com.github.kalininaleksandrv.simpletracker.exception.GlobalExceptionHandler;
import com.github.kalininaleksandrv.simpletracker.model.Story;
import com.github.kalininaleksandrv.simpletracker.model.StoryStatus;
import com.github.kalininaleksandrv.simpletracker.repository.DeveloperRepository;
import com.github.kalininaleksandrv.simpletracker.repository.IssueBaseRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class IssueControllerTest {

    MockMvc mockMvc;

    @Autowired
    private IssueController controller;

    @Autowired
    IssueBaseRepository issueBaseRepository;

    @Autowired
    DeveloperRepository developerRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    void saveHappyPath() throws Exception {

        ResultActions resultActions = mockMvc.perform(post("/api/v1/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"story1\", \n" +
                        "\"issueType\": \"STORY\", \n" +
                        "\"description\" : \"add feature1\", \n" +
                        "\"points\" : 10}"));
        assertNotNull(resultActions);

        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.issueId", Matchers.notNullValue()))
                .andExpect(jsonPath("$.dateTime", Matchers.notNullValue()))
                .andExpect(jsonPath("$.storyStatus", Matchers.is("NEW")));
    }

    @Test
    void saveNoIssueType() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/v1/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"story1\", \n" +
                        "\"issueType\": \"\", \n" +
                        "\"description\" : \"add feature1\", \n" +
                        "\"points\" : 10}"));
        assertNotNull(resultActions);

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsStringIgnoringCase("known type ids = [BUG, STORY]")));
    }

    @Test
    void saveStoryNoEstimation() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/v1/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"story1\", \n" +
                        "\"issueType\": \"STORY\", \n" +
                        "\"description\" : \"add feature1\" \n" +
                        "}"));
        assertNotNull(resultActions);

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("story must be estimate before creation")));
    }

    @Test
    void saveStoryWithDefinedDeveloper() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/v1/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"story1\", \n" +
                        "\"issueType\": \"STORY\", \n" +
                        "\"description\" : \"add feature1\", \n" +
                        "\"developer\" : \n" +
                        "        {\n" +
                        "        \"id\" : 2, \n" +
                        "        \"name\" : \"dev1\" \n" +
                        "        },\n" +
                        "\"points\" : 10}"));
        assertNotNull(resultActions);

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("story must not be assigned due creation")));
    }

    @Test
    void saveStoryToManyPoints() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/v1/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"story1\", \n" +
                        "\"issueType\": \"STORY\", \n" +
                        "\"description\" : \"add feature1\", \n" +
                        "\"points\" : 11}"));
        assertNotNull(resultActions);

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("one story must not exceed 11 points, try to decompose this story")));
    }

    @Test
    void saveBugNoPriority() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/v1/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"bug1\", \n" +
                        "\"issueType\": \"BUG\", \n" +
                        "\"description\" : \"fix bug1\" \n" +
                        "}"));
        assertNotNull(resultActions);

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("bug must be prioritized due creation")));
    }

    @Test
    void saveBugWrongPriorityType() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/v1/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"bug1\", \n" +
                        "\"issueType\": \"BUG\", \n" +
                        "\"description\" : \"fix bug1\", \n" +
                        "\"bugPriority\" : \"AAA\"}"));
        assertNotNull(resultActions);

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsStringIgnoringCase("not one of the values accepted for Enum class: [MAJOR, MINOR, CRITICAL]")));
    }

    @Test
    void updateHappyPath() throws Exception {

        mockMvc.perform(post("/api/v1/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"story1\", \n" +
                        "\"issueType\": \"STORY\", \n" +
                        "\"description\" : \"add feature1\", \n" +
                        "\"points\" : 10}"));

        String byId = issueBaseRepository.findById(1L).get().getIssueId();

        ResultActions resultActions = mockMvc.perform(put("/api/v1/issue/"+byId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"updated\", \n" +
                        "\"issueType\": \"STORY\", \n" +
                        "\"description\" : \"add feature1\", \n" +
                        "\"storyStatus\" : \"ESTIMATED\"}"));
        assertNotNull(resultActions);

        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is("updated")))
                .andExpect(jsonPath("$.storyStatus", Matchers.is("ESTIMATED")));

        Story updated = (Story) issueBaseRepository.findByIssueId(byId).get();
        Assertions.assertEquals(updated.getStoryStatus(), StoryStatus.ESTIMATED);
        Assertions.assertEquals(updated.getTitle(), "updated");
    }

    @Test
    void updateTypeMismatch() throws Exception {
        mockMvc.perform(post("/api/v1/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"story1\", \n" +
                        "\"issueType\": \"STORY\", \n" +
                        "\"description\" : \"add feature1\", \n" +
                        "\"points\" : 9}"));

        String byId = issueBaseRepository.findById(1L).get().getIssueId();

        ResultActions resultActions = mockMvc.perform(put("/api/v1/issue/"+byId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"title\" : \"updated\", \n" +
                        "\"issueType\": \"BUG\", \n" +
                        "\"description\" : \"add feature1\", \n" +
                        "\"storyStatus\" : \"ESTIMATED\"}"));
        assertNotNull(resultActions);

        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.containsStringIgnoringCase("new issue type not match with updated issue type")));

    }
}