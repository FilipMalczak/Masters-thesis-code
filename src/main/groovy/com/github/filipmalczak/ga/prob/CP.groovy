package com.github.filipmalczak.ga.prob

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

/**
 * Crossover probability.
 * @param < S > Specimen type (which will cross over with each other)
 */
interface CP<S extends Specimen> {
    int getCrossoverProbability(List<S> population, Context context)
}
