package com.github.filipmalczak.impl

import com.github.filipmalczak.ga.prob.MP
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical

@Canonical
class ConstMP<S extends Specimen> implements MP<S> {
    double mp

    @Override
    int getMutationProbability(List<S> population, Context context) {
        mp
    }
}