package com.github.filipmalczak.impl

import com.github.filipmalczak.ga.operators.NaturalSelectionOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical
import groovy.util.logging.Slf4j

import static com.github.filipmalczak.utils.RandomUtils.random

@Canonical
@Slf4j
class TourneyNaturalSelection<S extends Specimen> implements NaturalSelectionOperator<S> {
    int tourneySize = 3

    @Override
    List<S> selectNewPopulation(int populationSize, List<S> population, Context context) {
        def out = []
        while (out.size()<populationSize) {
            def candidates = [] as Set
            log.debug("Choosing candidates from $population")
            while (candidates.size() < tourneySize) {
                def newCandidate = random(population)
                log.trace("New candidate: $newCandidate")
                candidates.add(newCandidate)
            }
            log.trace("Candidates: $candidates")
            def winner = candidates.max { it.evaluate(context) }
            log.trace("Winner: $winner")
            out.add(winner)
        }
        out
    }


}
