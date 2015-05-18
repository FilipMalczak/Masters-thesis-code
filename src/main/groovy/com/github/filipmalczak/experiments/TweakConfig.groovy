package com.github.filipmalczak.experiments

import com.github.filipmalczak.datasets.ContextCategory
import com.github.filipmalczak.datasets.knapsack01.KnapsackResources
import com.github.filipmalczak.datasets.tsp.TSPResources
import com.github.filipmalczak.ea.operators.GenderSelectionOperator
import com.github.filipmalczak.ea.operators.NaturalSelectionOperator
import com.github.filipmalczak.ea.utils.EAUtils
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.choose_operator.RankRouletteChoose
import com.github.filipmalczak.impl.choose_operator.TourneyChoose
import com.github.filipmalczak.impl.gender_selection.NoGender
import com.github.filipmalczak.impl.knapsack01.KnapsackSetup
import com.github.filipmalczak.impl.natural_selection.NaturalSelection
import com.github.filipmalczak.impl.rastrigin.Point
import com.github.filipmalczak.impl.rastrigin.RastriginSetup
import com.github.filipmalczak.impl.tsp.TSPSetup
import com.github.filipmalczak.impl.tsp.Tour

import static com.github.filipmalczak.ea.utils.EAUtils.baseSetup
import static com.github.filipmalczak.ea.utils.EAUtils.knapsackSetup
import static com.github.filipmalczak.ea.utils.EAUtils.run
import static com.github.filipmalczak.ea.utils.EAUtils.tspSetup
import static com.github.filipmalczak.experiments.DataSetConfig.*

Storage.instance.init()

Map tweakParamSets(String name){
    Map tree = Storage.instance.treeSnapshot[name].initial
    return [
        popSize: [0, -10, -5, 5, 10].collect { tree.popSize + it }.findAll({it > 0}),
        maxGen: [0, -10, -5, 5, 10].collect { tree.maxGen + it }.findAll({it > 0}),
        mixinFactor: [0.0, -0.05, -0.1, 0.05, 0.1].collect { tree.mixinFactor + it }.findAll({it >= 0 && it<=1}),
        natSel: [ tree.natSel ],
        probs: [0.0, -0.02, -0.05, 0.02, 0.05].collect { x ->
            [0.0, -0.02, -0.05, 0.02, 0.05].collect { y ->
                [ tree.probs[0] + x, tree.probs[1] + y ]
            }
        }.sum().findAll { it[0]>=0 && it[0]<=1 && it[1]>=0 && it[1]<=1 }
    ]
}

def vals = [
    natSel: [
        roulette: new NaturalSelection<>(new RankRouletteChoose()),
        ts2: new NaturalSelection<>(new TourneyChoose(2)),
        ts3: new NaturalSelection<>(new TourneyChoose(3))
    ]
]

//tsp
if (executeDataset.tsp)
    new Explore(
        "tweak_tsp",
        3, 2,
        [ "popSize", "mixinFactor", "probs", "maxGen", "natSel"],
        tweakParamSets("tsp"),
        vals,
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
            Storage.instance.updateTree([tsp: [tweak: [(paramName): val]]])
        },
        { Map<Object, List> results ->
            results.keySet().min { k ->
                //best avg
                results[k].sum() { Context c -> c.globalBest.evaluate(c) } / results[k].size()
            }
        },
        [ "initial_tsp" ]
    ).run()

//knapsack
if (executeDataset.knapsack)
    new Explore(
        "tweak_knapsack",
        3, 2,
        [ "popSize", "mixinFactor", "probs", "maxGen", "natSel"],
        tweakParamSets("knapsack"),
        vals,
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
            Storage.instance.updateTree([knapsack: [tweak: [(paramName): val]]])
        },
        { Map<Object, List> results ->
            results.keySet().min { k ->
                //best avg
                results[k].sum() { Context c -> c.globalBest.evaluate(c) } / results[k].size()
            }
        },
        [ "initial_knapsack" ]
    ).run()