package com.github.filipmalczak.ea.utils

import com.github.filipmalczak.ea.alg.ContextHandler
import com.github.filipmalczak.ea.alg.GeneratePopulation
import com.github.filipmalczak.ea.alg.StopCondition
import com.github.filipmalczak.ea.operators.CrossoverOperator
import com.github.filipmalczak.ea.operators.GenderSelectionOperator
import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.ea.operators.NaturalSelectionOperator
import com.github.filipmalczak.ea.prob.CP
import com.github.filipmalczak.ea.prob.MP
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

interface EASetup<S extends Specimen> {
    int getPopulationSize()
    double getMixinFactor()

    ContextHandler<S> getContextHandler()
    GeneratePopulation<S> getGeneratePopulation()
    StopCondition<S> getStop()

    CrossoverOperator<S> getCrossover()
    MutationOperator<S> getMutation()
    NaturalSelectionOperator<S> getNaturalSelection()
    GenderSelectionOperator<S> getGenderSelection()

    CP<S> getCp()
    MP<S> getMp()

    Context<S> getContext()
}
