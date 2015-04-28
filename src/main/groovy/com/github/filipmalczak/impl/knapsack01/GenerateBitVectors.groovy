package com.github.filipmalczak.impl.knapsack01

import com.github.filipmalczak.ea.alg.AbstractGeneratePopulation
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.RandomUtils

class GenerateBitVectors extends AbstractGeneratePopulation<BitVector>{

    @Override
    BitVector generateWithoutGender(Context<BitVector> context) {
        new BitVector(
            RandomUtils.randomBools(context.problemDefinition.model.data.size(), 80)
        )
    }
}
