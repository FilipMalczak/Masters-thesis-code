package com.github.filipmalczak.impl.rastrigin

import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.RandomUtils


class MultiplyMutation implements MutationOperator<Point>{
    @Override
    List<Point> mutate(Point point, Context context) {
        Point out = point.copy()
        def idx = RandomUtils.random(out.coordinates.size())
        def multiplier = RandomUtils.random(0.1, 2.0)
        out.coordinates[idx] *= multiplier
        out
    }
}
