package com.github.filipmalczak.impl.choose_operator

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.utils.RandomUtils


class RankRouletteChoose<S extends Specimen> implements ChooseOperator<S> {
    @Override
    S chooseOne(List<S> population, Context<S> context, List<S> otherParents) {
        population.sort(true) {
            it.evaluate(context)
        }
        int popSize = population.size()
        int wheelSize = popSize*(popSize+1)/2
        int chosen = RandomUtils.random(wheelSize)
        choose(population, chosen)
    }

    static <T> T choose(List<T> population, int chosenIdx){
        int popSize = population.size()
        int counter = 0
        int idx = 0
        while (counter + popSize - idx < chosenIdx) {
            counter += popSize - idx
            idx++
        }
        population[idx]
    }
}
