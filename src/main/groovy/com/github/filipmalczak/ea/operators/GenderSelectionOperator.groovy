package com.github.filipmalczak.ea.operators

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen


interface GenderSelectionOperator<S extends Specimen> {
    /**
     *
     * @return List of lists of specimen. Each list (inner one) will be passed to CrossoverOperator to obtain children.
     * Specimens used in single crossover must have different genders
     */
    List<List<S>> selectParentSets(List<S> population, int popSize, Context context)
}