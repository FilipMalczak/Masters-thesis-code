package com.github.filipmalczak.impl.choose_operator

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen


interface ChooseOperator<S extends Specimen> {
    S chooseOne(List<S> population, Context<S> context, List<S> otherParents)
}
