package com.github.filipmalczak.ea.alg

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.utils.RandomUtils


abstract class AbstractGeneratePopulation<S extends Specimen> implements GeneratePopulation<S> {
    int genders = 1

    @Override
    List<S> generate(int size, Context<S> context) {
        def out = []
        if (size)
            size.times {
                out << generateOne(context)
            }
        out
    }

    S generateOne(Context<S> context) {
        S out = generateWithoutGender(context)
        out.gender = RandomUtils.random(genders)
        out
    }

    abstract S generateWithoutGender(Context<S> context)
}
