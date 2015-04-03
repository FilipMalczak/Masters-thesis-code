package com.github.filipmalczak.impl.choose_operator

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.utils.RandomUtils

import groovy.transform.Canonical

@Canonical
class TourneyChoose<S extends Specimen> implements ChooseOperator<S> {
    int size

    @Override
    S chooseOne(List<S> population, Context<S> context, List<S> otherParents) {
        Set<S> tourney = [] as Set
        while (tourney.size()<size)
            tourney.add RandomUtils.random(population)
        tourney.max { it.evaluate(context) }
    }
}
