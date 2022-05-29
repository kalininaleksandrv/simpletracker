package com.github.kalininaleksandrv.simpletracker.utils;

import com.github.kalininaleksandrv.simpletracker.exception.IssueProcessingException;
import com.github.kalininaleksandrv.simpletracker.model.Bug;
import com.github.kalininaleksandrv.simpletracker.model.Issue;
import com.github.kalininaleksandrv.simpletracker.model.IssueType;
import com.github.kalininaleksandrv.simpletracker.model.Story;

public final class IssueValidator {

    public static void validate(Issue issue) {
        if (issue.getIssueType() == IssueType.STORY && issue.getDeveloper() != null) {
            throw new IssueProcessingException("story must not be assigned due creation");
        }
        if (issue instanceof Story) {
            Story story = (Story) issue;
            if (story.getPoints() <= 0) {
                throw new IssueProcessingException("story must be estimate before creation");
            }
        }
        if (issue instanceof Bug) {
            Bug bug = (Bug) issue;
            if (bug.getBugPriority() == null) {
                throw new IssueProcessingException("bug must be prioritized before creation");
            }
        }
    }
}
