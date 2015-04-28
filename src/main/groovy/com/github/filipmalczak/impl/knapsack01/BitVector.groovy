package com.github.filipmalczak.impl.knapsack01

import com.github.filipmalczak.datasets.knapsack01.KnapsackModel
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.utils.RandomUtils

import groovy.json.JsonBuilder
import groovy.transform.Canonical
import groovy.transform.Memoized
import groovy.util.logging.Slf4j

@Canonical
@Slf4j
class BitVector implements Specimen{
    List<Boolean> vector
    int gender

    @Memoized(maxCacheSize = 5000)  //todo: tweak!
    static Map calculatePriceAndVolume(List<Boolean> vector, KnapsackModel model){
        def price = 0
        def volume = 0
        vector.eachWithIndex { boolean entry, int i ->
            if (entry) {
                try {
                    price += model.data[i].first
                    volume += model.data[i].second
                } catch (Throwable t){
                    throw t
                }
            }
        }
        [price: price, volume: volume]
    }

    Map getPriceAndVolume(KnapsackModel model){
        calculatePriceAndVolume(vector, model)
    }


    static double getEvaluation(List<Boolean> vector, KnapsackModel model){
        def pv = calculatePriceAndVolume(vector, model)
        int overload = pv.volume - model.maxSize
        if (overload<0)
//            overload *= -0.5
            overload = 0

        return (overload-1)*pv.price
//        def exp = Math.log10(pv.price)
//        overload *= Math.pow(10.0, exp)
//        return overload - pv.price
//        return 1.0/(ps.price+1) + (overload > 0 ? 1.0*overload/model.maxSize : 0.0 )
    }

    @Override
    double evaluate(Context context) {
        getEvaluation(vector, context.problemDefinition.model)
    }

    @Override
    def getPhenotype(Context context) {
        new JsonBuilder(calculatePriceAndVolume(vector, context.problemDefinition.model)+
            [maxSize: context.problemDefinition.model.maxSize]).toString()
    }

    @Override
    Specimen copy() {
        new BitVector([*vector], gender)
    }
}
