package com.github.filipmalczak.impl.rastrigin

import com.github.filipmalczak.ea.operators.CrossoverOperator
import com.github.filipmalczak.heuristics.Context


class AvgCrossover implements CrossoverOperator<Point>{
    /**
     * @param parents
     * @param context
     * @return
     */
    @Override
    List<Point> crossOver(List<Point> parents, Context context) {
        parents.shuffle()
        Point out = parents[0].copy()
        parents.tail().each { parent ->
            out.coordinates.size().times { i ->
                out.coordinates[i] += parent.coordinates[i]
            }
        }
        out.coordinates.size().times { i ->
            out.coordinates[i] /= out.coordinates.size()
        }
        out
    }
}
