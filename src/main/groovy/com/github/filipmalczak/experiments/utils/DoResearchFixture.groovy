package com.github.filipmalczak.experiments.utils

import com.github.filipmalczak.impl.choose_operator.ChooseOperator
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.choose_operator.RankRouletteChoose
import com.github.filipmalczak.impl.choose_operator.TourneyChoose
import com.github.filipmalczak.impl.gender_selection.Harem
import com.github.filipmalczak.impl.gender_selection.NoGender
import com.github.filipmalczak.impl.gender_selection.WithGender
import com.github.filipmalczak.impl.natural_selection.NaturalSelection

import groovy.transform.Canonical
import groovy.util.logging.Slf4j
import groovyx.gpars.GParsPool

import static com.github.filipmalczak.ea.utils.EAUtils.baseSetup
import static com.github.filipmalczak.ea.utils.EAUtils.run

@Slf4j
@Canonical
class DoResearchFixture implements Runnable{
    String name
    int repeats
    Closure fillConfig // knapsackSetup or tspSetup
    int poolSize = 9

    @Lazy def gpool = new GParsPool()
    @Lazy def pool = gpool.createPool(poolSize)

    @Lazy tweaked = Storage.instance.treeSnapshot[name].tweak
    @Lazy Map tweakedSetup = {
        [
            generations: tweaked.maxGen,
            crossProb: tweaked.probs[0],
            mutProb: tweaked.probs[1],
            populationSize: tweaked.popSize,
            mixinFactor: tweaked.mixinFactor as double,
            naturalSelection: [
                roulette: new NaturalSelection<>(new RankRouletteChoose()),
                ts2: new NaturalSelection<>(new TourneyChoose(2)),
                ts3: new NaturalSelection<>(new TourneyChoose(3))
            ][tweaked.natSel],
            calculateGenerationStats: true
        ]
    }.call()

    static Map<String, ChooseOperator> chooseOperators = [
        random: new RandomChoose(),
        roulette: new RankRouletteChoose(),
        ts2: new TourneyChoose(2),
        ts3: new TourneyChoose(3)
    ]
    static Map<String, Closure> factories = [
        noGender: { Object... params -> new NoGender(DoResearchFixture.chooseOperators[params.head()]) },
        gender: { Object... params ->
            new WithGender( [
                    (0): DoResearchFixture.chooseOperators[params[0]],
                    (1): DoResearchFixture.chooseOperators[params[1]]
                ],
                params[2]
            )
        },
        harem: { Object... params ->
            new Harem(
                params[0],
                params[1],
                DoResearchFixture.chooseOperators[params[2]],
                DoResearchFixture.chooseOperators[params[3]],
                new RandomChoose(),
                0
            )
        }
    ]

    def lookupNoGenderRandom(int i){
        String researchKey = "noGender_random_${i}"
        String reusableKey = Explore.key(
            ["populationSize", "mixinFactor", "crossProb", "mutProb", "generations", "natSel"],
            tweakedSetup + [natSel: tweaked.natSel, iteration: i]
        )
        log.debug("Looking for key $researchKey in experiment $name")
        def result = Storage.instance.getResult(name, researchKey)
        if (result == null) {
            log.debug("Looking for key $reusableKey in experiment tweak_$name")
            result = Storage.instance.getResult("tweak_$name", reusableKey)
            if (result != null) {
                log.debug("Found in tweak_$name")
                Storage.instance.putResult(name, researchKey, result)
            }
        } else {
            log.debug("Found in $name")
        }

        if (result == null) {
            log.debug "No saved result, calling body"
            def config = baseSetup(
                fillConfig(
                    tweakedSetup + [
                        gendersCount: 1,
                        genderSelection: new NoGender(new RandomChoose())
                    ]
                )
            )
            run(config)
            result = config.context
            Storage.instance.putResult(name, researchKey, result)
        }
    }

    def lookupGender(String genderSelectionName, int iter, Map setup, String first, String second, boolean dychotomy){
        String researchKey = "${genderSelectionName}_${[first, second, "${dychotomy}"].join("_")}_$iter"
        log.debug("Looking for key $researchKey in experiment $name")
        def result = Storage.instance.getResult(name, researchKey)

        if (result == null) {
            String reverseKey = "${genderSelectionName}_${[second, first, "${dychotomy}"].join("_")}_$iter"
            log.debug("Looking for key $reverseKey in experiment $name")
            result = Storage.instance.getResult(name, reverseKey)
            if (result==null) {
                log.debug "No saved result, calling body"
                def config = baseSetup(
                    fillConfig(
                        tweakedSetup + setup + [
                            genderSelection: factories[genderSelectionName].call(first, second, dychotomy)
                        ]
                    )
                )
                run(config)
                result = config.context
                Storage.instance.putResult(name, researchKey, result)
            } else {
                log.debug("Found result with reverse choose operators! Key: $reverseKey")
            }

        } else {
            log.debug("Found in $name")
        }
    }

    def lookup(String genderSelectionName, int iter, Map setup, Object... params){
        String researchKey = "${genderSelectionName}_${params.join("_")}_$iter"
        log.debug("Looking for key $researchKey in experiment $name")
        def result = Storage.instance.getResult(name, researchKey)

        if (result == null) {
            log.debug "No saved result, calling body"
            def config = baseSetup(
                fillConfig(
                    tweakedSetup + setup +[
                        genderSelection: factories[genderSelectionName].call(params)
                    ]
                )
            )
            run(config)
            result = config.context
            Storage.instance.putResult(name, researchKey, result)
        } else {
            log.debug("Found in $name")
        }
    }

    void noGender() {
        log.info "#############"
        log.info("# NoGender")
        log.info "#############"
        log.info("# NoGender(random)")
        gpool.withExistingPool(pool) {
            (0..(repeats - 1)).eachParallel { int i ->
                lookupNoGenderRandom(i)
            }
        }
        gpool.withExistingPool(pool) {
            ["roulette", "ts2", "ts3"].eachParallel { String choose ->
                log.info("# NoGender($choose)")
                gpool.withExistingPool(pool) {
                    (0..(repeats - 1)).eachParallel { int i ->
                        lookup("noGender", i, [gendersCount: 1], choose)
                    }
                }
            }
        }
    }

    void gender(boolean dychotomy){
        log.info "#############"
        log.info("# Gender (with${!dychotomy ? "out": ""} gender dychotomy)")
        log.info "#############"
        gpool.withExistingPool(pool) {
            ["roulette", "ts2", "ts3"].eachParallel { String chooseFirst ->
//            ["ts2"].eachParallel { String chooseFirst ->
                gpool.withExistingPool(pool) {
                    ["random", "roulette", "ts2", "ts3"].eachParallel { String chooseSecond ->
//                    ["ts3"].eachParallel { String chooseSecond ->
                        log.info("# Gender($chooseFirst, $chooseSecond, $dychotomy)")
                        gpool.withExistingPool(pool) {
                            (0..(repeats - 1)).eachParallel { int i ->
                                lookup("gender", i, [gendersCount: 2], chooseFirst, chooseSecond, dychotomy)
                            }
                        }
                    }
                }
            }
        }
    }

    void harem(){
        log.info "#############"
        log.info("# Harem")
        log.info "#############"
        gpool.withExistingPool(pool) {
            [1, 3, 5].eachParallel { int alphaCount ->
                gpool.withExistingPool(pool) {
                    [0.0, 0.1, 0.25].eachParallel { double alphaFactor ->
                        gpool.withExistingPool(pool) {
                            ["random", "roulette", "ts2", "ts3"].eachParallel { String alpha ->
                                gpool.withExistingPool(pool) {
                                    ["random", "roulette", "ts2", "ts3"].eachParallel { String beta ->
                                        log.info "# Harem($alphaCount, $alphaFactor, $alpha, $beta, random, 0)"
                                        gpool.withExistingPool(pool) {
                                            (0..(repeats - 1)).eachParallel { int i ->
                                                lookup(
                                                    "harem", i, [gendersCount: 2],
                                                    alphaCount,
                                                    alphaFactor,
                                                    alpha,
                                                    beta
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    void run() {
        log.info "|-----------------------------------------------------------|"
        log.info "|                                                           |"
        log.info "| Research experiment: $name${" "*(36-name.size())} |"
        log.info "|                                                           |"
        log.info "|-----------------------------------------------------------|"
        log.info "Base setup is: $tweaked"
        noGender()
        gpool.withExistingPool(pool) {
            [true, false].eachParallel { boolean dychotomy ->
                gender(dychotomy)
            }
        }
        harem()
    }
}
