package com.github.filipmalczak.datasets

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

class ContextCategory {
    static <S extends Specimen> Context<S> toContext(def model){
        def out = new Context<S>()
        out.problemDefinition.model = model
        out
    }
}
