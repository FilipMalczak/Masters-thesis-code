package com.github.filipmalczak.impl.choose_operator

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical

@Canonical
class MaxDiversityChoose<S extends Specimen> implements ChooseOperator<S>{
    Distance<S> distance

    static interface Distance<T>{
        /**
         * t1 == t2 => return 0
         * t1 != t2 => return x; x>0
         */
        double calculate(T t1, T t2)
    }

    @Override
    S chooseOne(List<S> population, Context<S> context, List<S> otherParents) {
        assert otherParents //todo: exception
        population.max { S analyzed ->
            otherParents.sum { distance.calculate(analyzed, it) }
        }
    }
}
