package com.github.filipmalczak.impl.rastrigin

import com.github.filipmalczak.ea.alg.GeneratePopulation
import com.github.filipmalczak.ea.operators.CrossoverOperator
import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.ea.utils.BaseEASetup


abstract class RastriginSetup extends BaseEASetup<Point>{
    abstract int getDimensions()

    @Override
    GeneratePopulation<Point> getGeneratePopulation() {
        new GeneratePoints(dimensions).with {
            genders = gendersCount
            it
        }
    }

    @Override
    CrossoverOperator<Point> getCrossover() {
        new AvgCrossover()
    }

    @Override
    MutationOperator<Point> getMutation() {
        new MultiplyMutation()
    }
}
