package com.github.filipmalczak.impl.gender_selection

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.impl.choose_operator.ChooseOperator

import groovy.transform.Canonical

@Canonical
class NoGender<S extends Specimen> extends AbstractGenderSelection<S>{
    ChooseOperator<S> chooseOperator
    int neededParents = 2

    @Override
    boolean usesGender() {
        return false
    }

    @Override
    List<S> generateParentSet(List<S> population, Context context, Map<Integer, List<S>> grouped) {
        def parents = []
        neededParents.times {
            parents << chooseOperator.chooseOne(population, context, parents)
        }
        parents
    }
}
