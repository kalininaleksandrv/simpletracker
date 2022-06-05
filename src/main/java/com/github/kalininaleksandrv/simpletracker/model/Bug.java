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
public class Bug extends Issue{
    @Enumerated(EnumType.STRING)
    private BugStatus bugStatus;

    @Enumerated(EnumType.STRING)
    private BugPriority bugPriority;

    @Override
    public void setStatusToNew() {
        this.setBugStatus(BugStatus.NEW);
    }

    @Override
    public Integer plane(Developer devToAllocate, Integer hours) {
        throw new IssueProcessingException("bug is not allowed to add to plan");
    }

    @Override
    public void unplane() {
        throw new IssueProcessingException("bug is not allowed to remove from plane");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Bug bug = (Bug) o;

        if (bugStatus != bug.bugStatus) return false;
        return bugPriority == bug.bugPriority;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (bugStatus != null ? bugStatus.hashCode() : 0);
        result = 31 * result + (bugPriority != null ? bugPriority.hashCode() : 0);
        return result;
    }
}
