package com.github.kalininaleksandrv.simpletracker.model;

import com.github.kalininaleksandrv.simpletracker.exception.IssueProcessingException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(allOf = Issue.class)
public class Story extends Issue {
    @Schema(required = true, defaultValue = "5")
    private int points;
    @Enumerated(EnumType.STRING)
    @Schema(hidden = true)
    private StoryStatus storyStatus;

    @Override
    public void setStatusToNew() {
        this.setStoryStatus(StoryStatus.NEW);
    }

    @Override
    public void plane(Developer devToAllocate) {
        super.setDeveloper(devToAllocate);
        storyStatus = StoryStatus.ESTIMATED;
    }

    @Override
    public void unplane() {
        if (storyStatus.equals(StoryStatus.COMPLETED)) {
            throw new IssueProcessingException("unable to unplan already completed story");
        }
        super.setDeveloper(null);
        storyStatus = StoryStatus.NEW;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Story story = (Story) o;

        if (points != story.points) return false;
        return storyStatus == story.storyStatus;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + points;
        result = 31 * result + (storyStatus != null ? storyStatus.hashCode() : 0);
        return result;
    }
}
