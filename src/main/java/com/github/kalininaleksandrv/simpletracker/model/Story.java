package com.github.kalininaleksandrv.simpletracker.model;

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
public class Story extends Issue {

    private int points;
    @Enumerated(EnumType.STRING)
    private StoryStatus storyStatus;

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

    @Override
    public void setStatusToNew() {
        this.setStoryStatus(StoryStatus.NEW);
    }

}
