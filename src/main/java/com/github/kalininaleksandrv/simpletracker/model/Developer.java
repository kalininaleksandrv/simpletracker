package com.github.kalininaleksandrv.simpletracker.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "developer")
@Data
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "name")
    private String name;
}
