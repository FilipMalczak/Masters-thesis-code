package com.github.filipmalczak.impl.knapsack01

import com.github.filipmalczak.datasets.knapsack01.KnapsackModel
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical
import groovy.transform.Memoized

@Canonical
class BitVector implements Specimen{
    List<Boolean> vector
    int gender

    @Memoized(maxCacheSize = 5000)  //todo: tweak!
    static double getEvaluation(List<Boolean> vector, KnapsackModel model){
        def price = 0
        def size = 0
        vector.eachWithIndex { boolean entry, int i ->
            if (entry) {
                price += model.data[i].first
                size += model.data[i].second
            }
        }
        def overload = size - model.maxSize
        return 1.0/(price+1) + (overload > 0 ? 1.0*overload/model.maxSize : 0.0 )
    }

    @Override
    double evaluate(Context context) {
        getEvaluation(vector, context.problemDefinition.model)
    }

    @Override
    Specimen copy() {
        new BitVector([*vector], gender)
    }
}
