package com.github.filipmalczak.experiments

import com.github.filipmalczak.ea.utils.EAUtils
import com.github.filipmalczak.experiments.utils.DoResearchFixture
import com.github.filipmalczak.experiments.utils.Explore
import com.github.filipmalczak.experiments.utils.ReproduceLiteratureFixtures
import com.github.filipmalczak.experiments.utils.Storage
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.choose_operator.RankRouletteChoose
import com.github.filipmalczak.impl.choose_operator.TourneyChoose
import com.github.filipmalczak.impl.gender_selection.NoGender
import com.github.filipmalczak.impl.natural_selection.NaturalSelection
import com.github.filipmalczak.impl.tsp.Tour

import groovyx.gpars.GParsPool

import static com.github.filipmalczak.ea.utils.EAUtils.baseSetup
import static com.github.filipmalczak.ea.utils.EAUtils.knapsackSetup
import static com.github.filipmalczak.ea.utils.EAUtils.run
import static com.github.filipmalczak.ea.utils.EAUtils.tspSetup

/**
 * Initial config
 */

new Explore(
    "tsp_initial",
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

new Explore(
    "knapsack_initial",
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

/**
 * Config tweaking
 */

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

new Explore(
    "tsp_tweak",
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
    [ "tsp_initial" ]
).run()

new Explore(
    "knapsack_tweak",
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
    [ "knapsack_initial" ]
).run()

/**
 * DSEA
 */

new DoResearchFixture(
    "tsp",
    5,
    EAUtils.&tspSetup
).run()

new DoResearchFixture(
    "knapsack",
    5,
    EAUtils.&knapsackSetup
).run()

/**
 * GGA and SexualGA
 */

int poolSize = 9
def gpool = new GParsPool()
def pool = gpool.createPool(poolSize)
gpool.withExistingPool(pool) {
    [
        ReproduceLiteratureFixtures.&doSexualGA, ReproduceLiteratureFixtures.&doGGA
    ].eachParallel { Closure fixture ->
        gpool.withExistingPool(pool) {
            ["tsp", "knapsack"].eachParallel { String modelName ->
                fixture(modelName, gpool, pool)
            }
        }
    }
}