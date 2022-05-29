package com.github.kalininaleksandrv.simpletracker.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "developer")
@Data
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
