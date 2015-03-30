package com.github.filipmalczak.impl.tsp

import com.github.filipmalczak.ea.alg.AbstractGeneratePopulation
import com.github.filipmalczak.heuristics.Context

class GenerateTours extends AbstractGeneratePopulation<Tour> {

    @Override
    Tour generateWithoutGender(Context<Tour> context) {
        def allIdxs = [ *(0..context.problemDefinition.model.points.size()-1) ]
        Collections.shuffle(allIdxs)
        new Tour(allIdxs, context.problemDefinition.model)
    }
}
