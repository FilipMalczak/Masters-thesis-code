package com.github.filipmalczak.impl

import com.github.filipmalczak.ea.operators.GenderSelectionOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical

//todo: choose alpha on something else than evaluation
@Canonical
class Harem<S extends Specimen> implements GenderSelectionOperator<S>{
    int alphaGender
    double sneaksFactor

    @Override
    List<List<S>> selectParentSets(List<S> population, int popSize, Context context) {
        def outSize = Math.ceil(popSize*context.crossProb)
        def out = []
        def perGender = [:].withDefault { [] }
        population.each { perGender[it.gender] << it }
        def alpha = perGender[alphaGender].max { it.evaluate(context) }
        perGender[alphaGender].remove(alpha)
        def sneaksCount = Math.ceil(outSize*sneaksFactor)
        outSize.times {
            def set = []
            perGender.each { gender, specimens ->
                def chosenOne
                if (gender == alphaGender && it >sneaksCount)
                    chosenOne = alpha
                else {
                    chosenOne = random(specimens) // oooooh its baaaad luck to be youuuuuu...
                    specimens.remove(chosenOne)
                }
                set << chosenOne
            }
            out.add(set)
        }
        out
    }
}
