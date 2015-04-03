package com.github.filipmalczak.impl.choose_operator

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.utils.RandomUtils


class RandomChoose<S extends Specimen> implements ChooseOperator<S>{
    @Override
    S chooseOne(List<S> population, Context<S> context, List<S> otherParents) {
        RandomUtils.random(population)
    }
}
