package com.github.filipmalczak.impl.example_msga

import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.heuristics.Context

import static com.github.filipmalczak.utils.RandomUtils.random


class Mutation implements MutationOperator<Point>{
    @Override
    List<Point> mutate(Point point, Context context) {
        new Point(
            point.x * random(0.1, 0.5),
            point.y * random(0.1, 0.5),
            point.gender
        )
    }
}
