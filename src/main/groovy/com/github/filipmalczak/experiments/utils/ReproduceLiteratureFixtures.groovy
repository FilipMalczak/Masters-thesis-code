package com.github.filipmalczak.experiments.utils

import com.github.filipmalczak.datasets.ContextCategory
import com.github.filipmalczak.datasets.knapsack01.KnapsackResources
import com.github.filipmalczak.datasets.tsp.TSPResources
import com.github.filipmalczak.ea.utils.EAUtils
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.impl.choose_operator.ChooseOperator
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.choose_operator.RankRouletteChoose
import com.github.filipmalczak.impl.choose_operator.TourneyChoose
import com.github.filipmalczak.impl.gender_selection.WithGender
import com.github.filipmalczak.impl.natural_selection.NaturalSelection

import groovy.util.logging.Slf4j

@Slf4j
class ReproduceLiteratureFixtures {
    static Map tweakedSetup(String modelName) {
        Map tweaked = Storage.instance.treeSnapshot[modelName].tweak
        [
            generations             : tweaked.maxGen,
            crossProb               : tweaked.probs[0],
            mutProb                 : tweaked.probs[1],
            populationSize          : tweaked.popSize,
            mixinFactor             : tweaked.mixinFactor as double,
            naturalSelection        : new NaturalSelection<>(new RandomChoose()),
            calculateGenerationStats: true
        ]
    }

    static Map<String, ChooseOperator> chooseOperators = [
        random  : new RandomChoose(),
        roulette: new RankRouletteChoose(),
        ts2     : new TourneyChoose(2),
        ts3     : new TourneyChoose(3)
    ]

    static Map getSetup(Map customSetup, String modelName) {
        EAUtils.baseSetup(
            EAUtils.&"${modelName}Setup"(
                tweakedSetup(modelName) + customSetup
            )
        )
    }

    static def getContext(String modelName){
        ContextCategory.toContext([
            tsp: TSPResources.sahara,
            knapsack: KnapsackResources.medium
        ][modelName])
    }

    static void doSexualGA(String modelName, def gpool, def pool, int repeats = 5) {
        String expName = modelName + "_sexual_ga"
        gpool.withExistingPool(pool) {
            ["roulette", "ts2", "ts3"].eachParallel { String choose1 ->
                gpool.withExistingPool(pool) {
                    ["random", "roulette", "ts2", "ts3"].eachParallel { String choose2 ->
                        gpool.withExistingPool(pool) {
                            (0..(repeats - 1)).eachParallel { int i ->
                                def key = "${choose1}_${choose2}_${i}"
                                log.info "Looking for key $key in experiment $expName"
                                Context result = Storage.instance.getResult(expName, key)
                                if (result == null) {
                                    log.info "Key $key (experiment: $expName) not found, running."
                                    result = getContext(modelName)
                                    EAUtils.run(
                                        getSetup(
                                            modelName,
                                            iteration: i,
                                            gendersCount: 2,
                                            genderSelection: new WithGender<>(
                                                [
                                                    (0): chooseOperators[choose1],
                                                    (1): chooseOperators[choose2]
                                                ],
                                                false
                                            ),
                                            context: result
                                        )
                                    )
                                    Storage.instance.putResult(expName, key, result)
                                    log.info "Result evaluates to ${result.globalBest.evaluate(result)} " +
                                        "with phenotype ${result.globalBest.getPhenotype(result)}"
                                } else {
                                    log.info "Key $key found in experiment: $expName"
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static void doGGA(String modelName, def gpool, def pool, int repeats = 5) {
        String expName = modelName + "_gga"
        gpool.withExistingPool(pool) {
            ["roulette", "ts2", "ts3"].eachParallel { String choose ->
                gpool.withExistingPool(pool) {
                    (0..(repeats - 1)).eachParallel { int i ->
                        def key = "${choose}_${i}"
                        log.info "Looking for key $key in experiment $expName"
                        Context result = Storage.instance.getResult(expName, key)
                        if (result == null) {
                            log.info "Key $key (experiment: $expName) not found, running."
                            result = getContext(modelName)
                            EAUtils.run(
                                getSetup(
                                    modelName,
                                    iteration: i,
                                    gendersCount: 2,
                                    genderSelection: new WithGender<>(
                                        [
                                            (0): chooseOperators["random"],
                                            (1): chooseOperators["random"]
                                        ],
                                        true
                                    ),
                                    naturalSelection: new NaturalSelection<>(chooseOperators[choose]),
                                    context: result
                                )
                            )
                            Storage.instance.putResult(expName, key, result)
                            log.info "Result evaluates to ${result.globalBest.evaluate(result)} " +
                                "with phenotype ${result.globalBest.getPhenotype(result)}"
                        } else {
                            log.info "Key $key found in experiment: $expName"
                        }
                    }
                }
            }
        }
    }
}
