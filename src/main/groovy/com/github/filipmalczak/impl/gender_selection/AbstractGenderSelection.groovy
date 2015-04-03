package com.github.filipmalczak.impl.gender_selection

import com.github.filipmalczak.ea.operators.GenderSelectionOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen


abstract class AbstractGenderSelection<S extends Specimen> implements GenderSelectionOperator<S>{
    Map<Integer, List<S>> groupByGender(List<S> population){
        population.groupBy {
            it.gender
        }
    }

    @Override
    List<List<S>> selectParentSets(List<S> population, int popSize, Context context) {
        def outSize = Math.ceil(popSize*context.crossProb)
        def out = []
        def perGender = usesGender()? groupByGender(population) : null
        outSize.times {
            out.add(generateParentSet(population, context, perGender))
        }
        out
    }

    abstract boolean usesGender()
    abstract List<S> generateParentSet(List<S> population, Context context, Map<Integer, List<S>> grouped)
}
