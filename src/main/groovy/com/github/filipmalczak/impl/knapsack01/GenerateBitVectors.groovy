package com.github.filipmalczak.impl.knapsack01

import com.github.filipmalczak.ea.alg.AbstractGeneratePopulation
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.RandomUtils

import groovy.transform.Canonical
import groovy.util.logging.Slf4j

@Canonical
@Slf4j
class GenerateBitVectors extends AbstractGeneratePopulation<BitVector>{
    int genders = 1

    @Override
    BitVector generateWithoutGender(Context<BitVector> context) {
        log.debug "Model: ${context.problemDefinition.model}"
        new BitVector(
            RandomUtils.randomBools(context.problemDefinition.model.data.size(), 80)
        )
    }
}
