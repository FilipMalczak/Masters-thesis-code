package com.github.filipmalczak.ea.operators

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

interface NaturalSelectionOperator<S extends Specimen> {
    /**
     * Returned population should be sorted from best to worst kept specimen.
     */
    List<S> selectNewPopulation(int populationSize, List<S> population, Context context)
}
