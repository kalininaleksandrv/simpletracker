package com.github.kalininaleksandrv.simpletracker.repository;

import com.github.kalininaleksandrv.simpletracker.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}
