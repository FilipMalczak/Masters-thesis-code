package com.github.filipmalczak.impl.knapsack01

import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.RandomUtils


class InverseMutation implements MutationOperator<BitVector>{
    @Override
    List<BitVector> mutate(BitVector bitVector, Context context) {
        new BitVector(
            bitVector.vector.collect {
                RandomUtils.happens(context.mutProb) ? !it : it
            },
            bitVector.gender
        )
    }
}
