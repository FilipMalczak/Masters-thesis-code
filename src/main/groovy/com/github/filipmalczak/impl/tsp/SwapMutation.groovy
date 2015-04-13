package com.github.filipmalczak.impl.tsp

import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.heuristics.Context

import static com.github.filipmalczak.utils.RandomUtils.random

class SwapMutation implements MutationOperator<Tour>{
    @Override
    List<Tour> mutate(Tour tour, Context context) {
        Tour out = tour.copy()
        def idx1 = random(tour.pointNumbers.size())
        def idx2 = random(tour.pointNumbers.size())
        while (idx2==idx1)
            idx2 = random(tour.pointNumbers.size())
        def buffer = out.pointNumbers[idx1]
        out.pointNumbers[idx1] = out.pointNumbers[idx2]
        out.pointNumbers[idx2] = buffer
        [ out ]
    }
}
