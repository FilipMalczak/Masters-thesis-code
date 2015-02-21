package com.github.filipmalczak.ga.prob

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

/**
 * Mutation probability.
 * @param < S > Specimen type (which will be mutated)
 */
interface MP<S extends Specimen> {
    int getMutationProbability(List<S> population, Context context)
}
