package com.github.filipmalczak.ea.utils

import com.github.filipmalczak.datasets.ContextCategory
import com.github.filipmalczak.ea.alg.ContextHandler
import com.github.filipmalczak.ea.alg.StopCondition
import com.github.filipmalczak.ea.prob.CP
import com.github.filipmalczak.ea.prob.MP
import com.github.filipmalczak.ea.utils.stdimpl.ConstCP
import com.github.filipmalczak.ea.utils.stdimpl.ConstMP
import com.github.filipmalczak.ea.utils.stdimpl.Handler
import com.github.filipmalczak.ea.utils.stdimpl.Stop
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

abstract class BaseEASetup<S extends Specimen> implements EASetup<S>{
    abstract int getGenerations()

    abstract double getCrossProb()
    abstract double getMutProb()

    abstract int getGendersCount()

    abstract def getModel()

    boolean calculatePopulationStats(){
        false
    }

    @Override
    ContextHandler<S> getContextHandler() {
        new Handler<S>()
    }

    @Override
    StopCondition<S> getStop() {
        new Stop<S>(generations)
    }

    @Override
    CP<S> getCp() {
        new ConstCP<S>(crossProb)
    }

    @Override
    MP<S> getMp() {
        new ConstMP<S>(mutProb)
    }

    @Override
    Context<S> getContext() {
        use(ContextCategory) {
            model ? model.toContext() : new Context<S>()
        }
    }
}
