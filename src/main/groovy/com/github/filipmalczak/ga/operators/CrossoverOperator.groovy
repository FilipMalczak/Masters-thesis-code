package com.github.filipmalczak.ga.operators

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

interface CrossoverOperator<S extends Specimen> {
    /**
     *
     * @param parents List of parent specimens, each with different gender
     * @param context
     * @return List of children (specimens)
     */
    List<S> crossOver(List<S> parents, Context context)
}
