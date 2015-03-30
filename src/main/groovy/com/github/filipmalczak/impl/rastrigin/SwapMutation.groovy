package com.github.filipmalczak.impl.rastrigin

import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.RandomUtils


class SwapMutation implements MutationOperator<Point> {
    @Override
    List<Point> mutate(Point point, Context context) {
        Point out = point.copy()
        def idx1 = RandomUtils.random(out.coordinates.size())
        def idx2 = RandomUtils.random(out.coordinates.size())
        while (idx2 == idx1)
            idx2 = RandomUtils.random(out.coordinates.size())
        out.coordinates[idx1] = point.coordinates[idx2]
        out.coordinates[idx2] = point.coordinates[idx1]
        out
    }
}
