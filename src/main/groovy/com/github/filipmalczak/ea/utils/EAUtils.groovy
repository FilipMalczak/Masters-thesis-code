package com.github.filipmalczak.ea.utils

import com.github.filipmalczak.datasets.ContextCategory
import com.github.filipmalczak.datasets.knapsack01.KnapsackResources
import com.github.filipmalczak.datasets.tsp.TSPResources
import com.github.filipmalczak.ea.EvolutionaryAlgorithm
import com.github.filipmalczak.ea.utils.stdimpl.ConstCP
import com.github.filipmalczak.ea.utils.stdimpl.ConstMP
import com.github.filipmalczak.ea.utils.stdimpl.Handler
import com.github.filipmalczak.ea.utils.stdimpl.Stop
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.impl.knapsack01.GenerateBitVectors
import com.github.filipmalczak.impl.knapsack01.InverseMutation
import com.github.filipmalczak.impl.knapsack01.OnePointCrossover
import com.github.filipmalczak.impl.tsp.GenerateTours
import com.github.filipmalczak.impl.tsp.MergeCrossover
import com.github.filipmalczak.impl.tsp.ReverseSequenceMutation

import groovy.util.logging.Slf4j

@Slf4j
class EAUtils {
    /**
     *
     * @deprecated Use #run(Map) instead
     */
    @Deprecated
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

    static <S extends Specimen> List<S> run(Map setup){
        log.debug "Setup: $setup"
        log.debug "Model: ${setup.model}"
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

    static Map baseSetup(Map rest){
        assert rest.keySet().containsAll("generations", "crossProb", "mutProb")
        use(ContextCategory) {
            def out = rest + [
                stop: new Stop(rest.generations),
                contextHandler: new Handler(rest.calculateGenerationStats ?: false),
                cp: new ConstCP(rest.crossProb),
                mp: new ConstMP(rest.mutProb),
                context: rest.context ?: rest.model!=null ? rest.model.toContext() : new Context()
            ]
            return out
        }
    }

    static Map knapsackSetup(Map rest){
        assert rest.containsKey("gendersCount")
        log.debug "Use model: medium ${KnapsackResources.medium}"
        def out =  rest + [
            generatePopulation: new GenerateBitVectors(rest.gendersCount),
            crossover: new OnePointCrossover(),
            mutation: new InverseMutation(),
            model: KnapsackResources.medium
        ]
        assert out.model
        return out
    }

    static Map tspSetup(Map rest){
        assert rest.containsKey("gendersCount")
        log.debug "Use model: sahara ${TSPResources.sahara}"
        def out = rest + [
            generatePopulation: new GenerateTours(rest.gendersCount),
            crossover: new MergeCrossover(),
            mutation: new ReverseSequenceMutation(),
            model: TSPResources.sahara
        ]
        assert out.model
        return out
    }
}
