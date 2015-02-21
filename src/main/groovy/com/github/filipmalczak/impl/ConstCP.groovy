package com.github.filipmalczak.impl

import com.github.filipmalczak.ga.prob.CP
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical

@Canonical
class ConstCP<S extends Specimen> implements CP<S> {
    double cp

    @Override
    int getCrossoverProbability(List<S> population, Context context) {
        cp
    }
}
