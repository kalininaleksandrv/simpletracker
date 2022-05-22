package com.github.kalininaleksandrv.simpletracker.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Bug extends Issue{

    private BugStatus bugStatus;
    private BugPriority priority;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Bug bug = (Bug) o;

        if (bugStatus != bug.bugStatus) return false;
        return priority == bug.priority;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (bugStatus != null ? bugStatus.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        return result;
    }
}
