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
public class Bug extends Issue{
    @Enumerated(EnumType.STRING)
    private BugStatus bugStatus;

    @Enumerated(EnumType.STRING)
    private BugPriority bugPriority;

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
