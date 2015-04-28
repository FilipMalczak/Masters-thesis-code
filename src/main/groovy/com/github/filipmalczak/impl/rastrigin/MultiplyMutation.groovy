package com.github.filipmalczak.impl.rastrigin

import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.utils.RandomUtils


class MultiplyMutation implements MutationOperator<Point>{
    @Override
    List<Point> mutate(Point point, Context context) {
        Point out = point.copy()
        out.coordinates.size().times { int idx ->
            if (RandomUtils.happens(context.mutProb)) {
                def multiplier = RandomUtils.normal(0.5, 1.5)
                while (Math.abs(multiplier * out.coordinates[idx]) > 5.12)
                    multiplier = RandomUtils.normal(0.5, 1.5)
                out.coordinates[idx] *= (RandomUtils.happens(500) ? -1 : 1) * multiplier
            }
        }
        [ out ]
    }
}
