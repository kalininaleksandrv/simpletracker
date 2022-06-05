package com.github.kalininaleksandrv.simpletracker.controller;

import com.github.kalininaleksandrv.simpletracker.model.Issue;
import com.github.kalininaleksandrv.simpletracker.model.Plan;
import com.github.kalininaleksandrv.simpletracker.model.Story;
import com.github.kalininaleksandrv.simpletracker.service.IssuePlaningService;
import com.github.kalininaleksandrv.simpletracker.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
@Slf4j
public class IssueController {

    private final IssueService issueService;
    private final IssuePlaningService issuePlaningService;

    @Operation(summary = "Get an Issue f.e. Story or Bug by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the issue",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Issue.class))}),
            @ApiResponse(responseCode = "400", description = "Request could not be processed",
                    content = @Content),
            @ApiResponse(responseCode = "204", description = "Issue not found",
                    content = @Content)})
    @GetMapping(path = "issues/{id}")
    public ResponseEntity<Issue> getIssueById(
            @Parameter(name = "id", description = "id of issue in UUID format", required = true)
            @PathVariable UUID id) {

        Optional<Issue> issue = issueService.getIssueByUid(id);
        return issue.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "save a new issue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "issue save successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Issue.class))}),
            @ApiResponse(responseCode = "400", description = "Request could not be processed",
                    content = @Content)})
    @PostMapping(path = "issue")
    public ResponseEntity<Issue> save(
            @Parameter(name = "issue",
                    description = "issue to save in JSON body, no id must be in a body",
                    schema = @Schema(implementation = Story.class))
            @RequestBody Issue issue) {
        var savedIssue = issueService.save(issue);
        return new ResponseEntity<>(savedIssue, HttpStatus.OK);
    }

    @Operation(summary = "update an existing issue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "issue updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Issue.class))}),
            @ApiResponse(responseCode = "400", description = "Request could not be processed",
                    content = @Content)})
    @PutMapping(path = "issue/{id}")
    public ResponseEntity<Issue> update(
            @Parameter(name = "id", description = "id of issue in UUID format", required = true)
            @PathVariable UUID id,
            @Parameter(name = "issue", description = "issue to update in JSON body, id must be provided",
                    schema = @Schema(implementation = Story.class))
            @RequestBody Issue issue) {
        issue.setIssueId(String.valueOf(id));
        var issueFromDb = issueService.update(issue);
        return new ResponseEntity<>(issueFromDb, HttpStatus.OK);
    }

    @Operation(summary = "delete an issue by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "issue deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Request could not be processed"),
            @ApiResponse(responseCode = "204", description = "Issue not found")})
    @DeleteMapping(path = "issue/{id}")
    public ResponseEntity<String> deleteById(
            @Parameter(name = "id", description = "id of issue in UUID format", required = true)
            @PathVariable UUID id) {
        if (issueService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "get all issue pageable")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "page of issues",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))}),
            @ApiResponse(responseCode = "400", description = "Request could not be processed",
                    content = @Content)})
    @GetMapping(path = "issues")
    public ResponseEntity<Page<Issue>> findAll(
            @Parameter(name = "page", description = "number of page to display, starts with 0", required = true)
            @RequestParam int page,
            @Parameter(name = "size", description = "number of elements on a page, 100 by default")
            @RequestParam(required = false, defaultValue = "100") int size) {
        if (page < 0) throw new IllegalArgumentException("page must be >= 0");
        Page<Issue> user = issueService.getAllIssues(page, size);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "get plan for currently added stories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "page of issues",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Plan.class))}),
            @ApiResponse(responseCode = "400", description = "Request could not be processed",
                    content = @Content)})
    @GetMapping(path = "/plan")
    public ResponseEntity<Plan> plan() {

        Plan plan = issuePlaningService.calculatePlan();
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }
}
