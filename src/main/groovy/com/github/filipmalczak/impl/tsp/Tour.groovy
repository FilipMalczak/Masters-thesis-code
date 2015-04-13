package com.github.filipmalczak.impl.tsp

import com.github.filipmalczak.datasets.tsp.TSPModel
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical
import groovy.transform.Memoized

/**
 * Assumption: tour is closed, so starting and ending point are the same.
 * Ergo, tours [x, y, z], [z, y, x], [y, z, x] (and some more) will have the same lengths.
 */
@Canonical
class Tour implements Specimen{
    List<Integer> pointNumbers
    int gender = 0

    @Memoized(maxCacheSize = 5000)  //todo: tweak!
    static double getLength(List<Integer> points, TSPModel usedModel){
        Number out = 0.0
        (points.size() + 1).times { int i ->
            out += usedModel.distance(
                points[i % usedModel.points.size()],
                points[(i+1) % usedModel.points.size()]
            )
        }
        out
    }

    @Override
    double evaluate(Context context) {
        getLength(pointNumbers, context.problemDefinition.model)
    }

    @Override
    Specimen copy() {
        new Tour([*pointNumbers], gender)
    }
}
