package com.github.kalininaleksandrv.simpletracker.model;

import com.github.kalininaleksandrv.simpletracker.exception.IssueProcessingException;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Story extends Issue {

    private int points;
    @Enumerated(EnumType.STRING)
    private StoryStatus storyStatus;

    @Override
    public void setStatusToNew() {
        this.setStoryStatus(StoryStatus.NEW);
    }

    @Override
    public Integer plane(Developer devToAllocate, Integer hours) {
        if (hours >= points) {
            super.setDeveloper(devToAllocate);
            storyStatus = StoryStatus.ESTIMATED;
            return hours - points;
        }
        return -1;
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
