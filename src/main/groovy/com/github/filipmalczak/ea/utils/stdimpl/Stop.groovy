package com.github.filipmalczak.ea.utils.stdimpl

import com.github.filipmalczak.ea.alg.StopCondition
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical

@Canonical
class Stop<S extends Specimen> implements StopCondition<S>{
    int maxGen = 50

    @Override
    boolean shouldStop(List<S> population, Context context) {
        context.generation == maxGen
    }
}
