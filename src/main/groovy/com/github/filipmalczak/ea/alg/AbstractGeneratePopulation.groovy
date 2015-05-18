package com.github.filipmalczak.ea.alg

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.utils.RandomUtils

import groovy.util.logging.Slf4j

@Slf4j
abstract class AbstractGeneratePopulation<S extends Specimen> implements GeneratePopulation<S> {
    abstract int getGenders() //todo: ugly as hell, refactor and use getGendersCount instead

    @Override
    int getGenderCount(){
        genders
    }

    @Override
    List<S> generate(int size, Context<S> context) {
        def out = []
        def genderHistogram = [:].withDefault { 0 }
        if (size)
            size.times {
                def generated = generateOne(context)
                out << generated
                genderHistogram[generated.gender]++
            }
        log.debug "Gender histogram: $genderHistogram"
        out
    }

    S generateOne(Context<S> context) {
        S out = generateWithoutGender(context)
        out.gender = RandomUtils.random(genders)
        out
    }

    abstract S generateWithoutGender(Context<S> context)
}
