package com.github.filipmalczak.impl.knapsack01

import com.github.filipmalczak.datasets.knapsack01.KnapsackModel
import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.RandomUtils


class InverseMutation implements MutationOperator<BitVector>{
    static final Map<String, Integer> probConstraints = [
        maxLitOverload: 700,
        minLitOverload: 200,
        maxLitUnderload: 200,
        minLitUnderload: 50,
        maxUnlitOverload: 150,
        minUnlitOverload: 50,
        maxUnlitUnderload: 60,
        minUnlitUnderload: 100
    ]

    static double overloadFactor(int size, KnapsackModel model){
//        Math.sqrt( (size-model.maxSize)/model.maxOverload )
        (size-model.maxSize)/model.maxOverload
    }

    static double underloadFactor(int size, KnapsackModel model){
//        Math.sqrt( (model.maxSize-size)/model.maxSize )
        (model.maxSize-size)/model.maxSize
    }

    static int litInverseProb(BitVector bitVector, KnapsackModel model){
        def pv = bitVector.getPriceAndVolume(model)
        double factor
        if (pv.volume > model.maxSize) {
            //the bigger overload the bigger probability that we want to throw this out
            factor = overloadFactor(pv.volume, model)
            return probConstraints.minLitOverload + Math.ceil(
                (probConstraints.maxLitOverload-probConstraints.minLitOverload)*factor
            )
        } else {
            //the bigger underload the smaller prob. that we want to throw this out
            factor = underloadFactor(pv.volume, model)
            return probConstraints.maxLitUnderload - Math.ceil(
                (probConstraints.maxLitUnderload-probConstraints.minLitUnderload)*factor
            )
        }
    }

    static int unlitInverseProb(BitVector bitVector, KnapsackModel model){
        def ps = bitVector.getPriceAndVolume(model)
        double factor
        if (ps.volume > model.maxSize) {
            //the bigger overload the smaller prob. that we want to take this
            factor = overloadFactor(ps.volume, model)
            return probConstraints.maxUnlitOverload - Math.ceil(
                (probConstraints.maxUnlitOverload-probConstraints.minUnlitOverload)*factor
            )

        } else {
            //the bigger underload the bigger prob. that we want to take this
            factor = underloadFactor(ps.volume, model)
            return probConstraints.minUnlitOverload + Math.ceil(
                (probConstraints.maxUnlitOverload-probConstraints.minUnlitOverload)*factor
            )
        }
    }

    @Override
    List<BitVector> mutate(BitVector bitVector, Context context) {
        [ new BitVector(
            bitVector.vector.collect {
                RandomUtils.happens(
                    it ?
                        litInverseProb(bitVector, context.problemDefinition.model):
                        unlitInverseProb(bitVector, context.problemDefinition.model)
                ) ? !it : it
            },
            bitVector.gender
        ) ]
    }
}
