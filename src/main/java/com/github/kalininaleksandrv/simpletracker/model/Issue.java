package com.github.kalininaleksandrv.simpletracker.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
public class Issue {

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

    @ManyToOne
    @JoinColumn(name = "developer_id", referencedColumnName = "id")
    Developer developer;

}
