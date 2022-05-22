package com.github.kalininaleksandrv.simpletracker.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "developers")
@Data
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;
}
