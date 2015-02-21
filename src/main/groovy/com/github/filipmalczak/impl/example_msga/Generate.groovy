package com.github.filipmalczak.impl.example_msga

import com.github.filipmalczak.ga.alg.GeneratePopulation
import com.github.filipmalczak.heuristics.Context

import static com.github.filipmalczak.utils.RandomUtils.random


class Generate implements GeneratePopulation<Point>{
    @Override
    List<Point> generate(int size, Context context) {
        (0..size-1).collect {
            new Point(random(0, 30), random(0, 30), random([0, 1]))
        }
    }
}
