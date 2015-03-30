package com.github.filipmalczak.ea.alg

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen


interface StopCondition<S extends Specimen> {
    boolean shouldStop(List<S> population, Context context)
}