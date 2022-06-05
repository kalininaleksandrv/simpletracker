package com.github.kalininaleksandrv.simpletracker.model;

public interface Planable {
    Integer plane(Developer developer, Integer hours);

    void unplane();
}
