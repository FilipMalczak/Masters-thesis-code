package com.github.filipmalczak.impl.tsp

import com.github.filipmalczak.ea.operators.CrossoverOperator
import com.github.filipmalczak.heuristics.Context

import static com.github.filipmalczak.utils.RandomUtils.random


class MergeCrossover implements CrossoverOperator<Tour>{
    /**
     * Assumes 2 parents.
     * Won't work for extremely low number of points, like 4.
     * @param parents
     * @param context
     * @return
     */
    @Override
    List<Tour> crossOver(List<Tour> parents, Context context) {
//        try {
//            assert parents.size() == 2
//        } catch (Throwable t){
//            throw t
//        }
        Tour tour1 = parents[0].copy()
        Tour tour2 = parents[1].copy()
        def cutPoint = (random(0.25, 0.75)* parents[0].pointNumbers.size()) as int
        def firstHead = tour1.pointNumbers.subList(0, cutPoint)
        def secondHead = tour2.pointNumbers
        secondHead.removeAll(firstHead)

        def out1 = firstHead + []
        tour2.pointNumbers.each {
            if (!out1.contains(it))
                out1.add(it)
        }
        def out2 = secondHead + []
        tour1.pointNumbers.each {
            if (!out2.contains(it))
                out2.add(it)
        }
//        assert out1.size() == out1.toSet().size()
//        assert out2.size() == out2.toSet().size()
        [ new Tour(out1, tour1.gender), new  Tour(out2, tour2.gender) ]
    }
}
