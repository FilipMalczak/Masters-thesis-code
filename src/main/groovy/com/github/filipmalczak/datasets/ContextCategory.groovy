package com.github.filipmalczak.datasets

import com.github.filipmalczak.datasets.tsp.TSPModel
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.impl.tsp.Tour


class ContextCategory {
    static Context<Tour> toContext(TSPModel model){
        def out = new Context<Tour>()
        out.problemDefinition.model = model
        out
    }
}
