package com.github.filipmalczak.impl.tsp

import com.github.filipmalczak.ea.alg.AbstractGeneratePopulation
import com.github.filipmalczak.heuristics.Context

import groovy.transform.Canonical

@Canonical
class GenerateTours extends AbstractGeneratePopulation<Tour> {
    int genders = 1

    @Override
    Tour generateWithoutGender(Context<Tour> context) {
        def allIdxs = [ *(0..context.problemDefinition.model.points.size()-1) ]
        Collections.shuffle(allIdxs)
        new Tour(allIdxs, context.problemDefinition.model)
    }
}
