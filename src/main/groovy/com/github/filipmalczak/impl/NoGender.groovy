package com.github.filipmalczak.impl

import com.github.filipmalczak.ga.operators.GenderSelectionOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import static com.github.filipmalczak.utils.RandomUtils.random


class NoGender<S extends Specimen> implements GenderSelectionOperator<S>{
    @Override
    List<List<S>> selectParentSets(List<S> population, int popSize, Context context) {
        def outSize = Math.ceil(popSize*context.crossProb)
        def out = []
        def perGender = [:].withDefault { [] }
        population.each { perGender[it.gender] << it }
        outSize.times {
            def set = []
            perGender.each { gender, specimens ->
                def chosenOne = random(specimens) // oooooh its baaaad luck to be youuuuuu...
                set << chosenOne
                specimens.remove(chosenOne)
            }
            out.add(set)
        }
        out
    }
}
