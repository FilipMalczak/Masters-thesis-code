package com.github.filipmalczak.impl.natural_selection

import com.github.filipmalczak.ea.operators.NaturalSelectionOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.impl.choose_operator.ChooseOperator

import groovy.transform.Canonical

@Canonical
class NaturalSelection<S extends Specimen> implements NaturalSelectionOperator<S>{
    ChooseOperator<S> chooseOperator

    @Override
    List<S> selectNewPopulation(int populationSize, List<S> population, Context context) {
        Set<S> out = [] as Set
        while (out.size()<populationSize)
            out << chooseOperator.chooseOne(population, context, null)
        out.asList()
    }
}
