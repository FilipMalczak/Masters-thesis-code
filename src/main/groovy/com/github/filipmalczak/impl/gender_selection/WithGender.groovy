package com.github.filipmalczak.impl.gender_selection

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.impl.choose_operator.ChooseOperator

import groovy.transform.Canonical

@Canonical
class WithGender<S extends Specimen> extends AbstractGenderSelection<S>{
    Map<Integer, ChooseOperator<S>> chooseOperators

    @Override
    boolean usesGender() {
        true
    }

    @Override
    List<S> generateParentSet(List<S> population, Context context, Map<Integer, List<S>> grouped) {
        def parents = []
        grouped.each { gender, specimens ->
            // oooooh its baaaad luck to be youuuuuu...
            def chosenOne = chooseOperators[gender].chooseOne(specimens, context, parents)
            parents << chosenOne
        }
        parents
    }

}
