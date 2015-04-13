package com.github.filipmalczak.impl.knapsack01

import com.github.filipmalczak.ea.alg.GeneratePopulation
import com.github.filipmalczak.ea.operators.CrossoverOperator
import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.ea.utils.BaseEASetup


abstract class KnapsackSetup extends BaseEASetup{
    @Override
    GeneratePopulation getGeneratePopulation() {
        new GenerateBitVectors()
    }

    @Override
    CrossoverOperator getCrossover() {
        new OnePointCrossover()
    }

    @Override
    MutationOperator getMutation() {
        new InverseMutation()
    }
}
