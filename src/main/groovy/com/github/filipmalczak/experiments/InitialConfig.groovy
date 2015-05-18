package com.github.filipmalczak.experiments

import static com.github.filipmalczak.ea.utils.EAUtils.*
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.choose_operator.RankRouletteChoose
import com.github.filipmalczak.impl.choose_operator.TourneyChoose
import com.github.filipmalczak.impl.gender_selection.NoGender
import com.github.filipmalczak.impl.natural_selection.NaturalSelection
import com.github.filipmalczak.impl.tsp.Tour
import static com.github.filipmalczak.experiments.DataSetConfig.*

Storage.instance.init()

////tsp
if (executeDataset.tsp)
    new Explore(
        "initial_tsp",
        5, 3,
        [ "popSize", "mixinFactor", "probs", "maxGen", "natSel"],
        [
            popSize: [ 10, 20, 50 ],
            mixinFactor: [0.0, 0.1, 0.25, 0.5],
            probs: [0.6, 0.7, 0.8].collect { cp -> [0.1, 0.2, 0.3].collect { mp -> [cp, mp] } }.sum(),
            maxGen: [25, 50, 100],
            natSel: ["roulette", "ts2", "ts3"]
        ],
        [
            natSel: [
                roulette: new NaturalSelection<>(new RankRouletteChoose()),
                ts2: new NaturalSelection<>(new TourneyChoose(2)),
                ts3: new NaturalSelection<>(new TourneyChoose(3))
            ]
        ],
        { Map<String, Object> realConfig ->
            def setup = baseSetup(tspSetup(
                generations: realConfig.maxGen,
                crossProb: realConfig.probs[0],
                mutProb: realConfig.probs[1],
                gendersCount: 1,
                populationSize: realConfig.popSize,
                mixinFactor: realConfig.mixinFactor,
                naturalSelection: realConfig.natSel,
                genderSelection: new NoGender<Tour>(new RandomChoose<Tour>())
            ))
            run(setup)
            setup.context
        },
        { String paramName, def val, def realVal ->
            Storage.instance.updateTree([tsp: [initial: [(paramName): val]]])
        },
        { Map<Object, List> results ->
            results.keySet().min { k ->
                def ctx =  results[k].min { Context c ->
                    c.globalBest.evaluate(c)
                }
                ctx.globalBest.evaluate(ctx)
            }
        }
    ).run()

//knapsack
if (executeDataset.knapsack)
    new Explore(
        "initial_knapsack",
        5, 3,
        [ "popSize", "mixinFactor", "probs", "maxGen", "natSel"],
        [
            popSize: [ 10, 20, 50 ],
            mixinFactor: [0.0, 0.1, 0.25, 0.5],
            probs: [0.6, 0.7, 0.8].collect { cp -> [0.1, 0.2, 0.3].collect { mp -> [cp, mp] } }.sum(),
            maxGen: [25, 50, 100],
            natSel: ["roulette", "ts2", "ts3"]
        ],
        [
            natSel: [
                roulette: new NaturalSelection<>(new RankRouletteChoose()),
                ts2: new NaturalSelection<>(new TourneyChoose(2)),
                ts3: new NaturalSelection<>(new TourneyChoose(3))
            ]
        ],
        { Map<String, Object> realConfig ->
            def setup = baseSetup(knapsackSetup(
                generations: realConfig.maxGen,
                crossProb: realConfig.probs[0],
                mutProb: realConfig.probs[1],
                gendersCount: 1,
                populationSize: realConfig.popSize,
                mixinFactor: realConfig.mixinFactor,
                naturalSelection: realConfig.natSel,
                genderSelection: new NoGender<Tour>(new RandomChoose<Tour>())
            ))
            run(setup)
            setup.context
        },
        { String paramName, def val, def realVal ->
            Storage.instance.updateTree([knapsack: [initial: [(paramName): val]]])
        },
        { Map<Object, List> results ->
            results.keySet().min { k ->
                def ctx =  results[k].min { Context c -> c.globalBest.evaluate(c) }
                ctx.globalBest.evaluate(ctx)
            }
        }
    ).run()