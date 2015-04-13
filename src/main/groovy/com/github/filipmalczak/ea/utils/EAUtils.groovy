package com.github.filipmalczak.ea.utils

import com.github.filipmalczak.ea.EvolutionaryAlgorithm
import com.github.filipmalczak.heuristics.Specimen

class EAUtils {
    static <S extends Specimen> List<S> run(EASetup<S> setup){
        setup.with {
            new EvolutionaryAlgorithm<S>(
                populationSize,
                mixinFactor,
                contextHandler,
                generatePopulation,
                stop,
                crossover,
                mutation,
                naturalSelection,
                genderSelection,
                cp,
                mp
            ).doRun(context)
        }

    }
}
