package com.github.filipmalczak.impl.tsp

import com.github.filipmalczak.ea.alg.GeneratePopulation
import com.github.filipmalczak.ea.operators.CrossoverOperator
import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.ea.utils.BaseEASetup


abstract class TSPSetup extends BaseEASetup<Tour>{

    @Override
    GeneratePopulation<Tour> getGeneratePopulation() {
        new GenerateTours(gendersCount)
    }

    @Override
    CrossoverOperator<Tour> getCrossover() {
        new MergeCrossover()
    }

    @Override
    MutationOperator<Tour> getMutation() {
        new ReverseSequenceMutation()
    }


}
