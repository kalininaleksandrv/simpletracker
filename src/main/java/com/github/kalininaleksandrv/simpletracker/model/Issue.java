package com.github.kalininaleksandrv.simpletracker.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "issueType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Story.class, name = "STORY"),
        @JsonSubTypes.Type(value = Bug.class, name = "BUG")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
public abstract class Issue implements Planable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Column(name = "issue_id")
    String issueId;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "date")
    LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id", referencedColumnName = "id")
    Developer developer;

    @Enumerated(EnumType.STRING)
    IssueType issueType;

    public void setStatusToNew() {
    }

}
