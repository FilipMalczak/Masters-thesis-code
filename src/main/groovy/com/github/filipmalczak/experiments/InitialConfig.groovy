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

////tsp
new Explore(
    "initial_tsp",
    5, 3,
    [ "popSize", "mixinFactor", "probs", "maxGen", "natSel"],
    [
        popSize: [ 10, 20, 50, 10 ],
        mixinFactor: [0.0, 0.1, 0.25, 0.5],
        probs: [0.6, 0.7, 0.8].collect { cp -> [0.1, 0.2, 0.3].collect { mp -> [cp, mp] } }.sum(),
        maxGen: [25, 50, 100, 200],
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
        def ctx = ContextCategory.toContext(TSPResources.xqf131)
        EAUtils.run(new TSPSetup() {
            @Override
            int getGenerations() {
                realConfig.maxGen
            }

            @Override
            double getCrossProb() {
                realConfig.probs[0]
            }

            @Override
            double getMutProb() {
                realConfig.probs[1]
            }

            @Override
            int getGendersCount() {
                1
            }

            @Override
            def getModel() {
                return null
            }

            @Override
            Context getContext(){
                ctx
            }

            @Override
            int getPopulationSize() {
                realConfig.popSize
            }

            @Override
            double getMixinFactor() {
                realConfig.mixinFactor
            }

            @Override
            NaturalSelectionOperator<Tour> getNaturalSelection() {
                realConfig.natSel
            }

            @Override
            GenderSelectionOperator<Tour> getGenderSelection() {
                new NoGender<Tour>(new RandomChoose<Tour>())
            }
        })
        ctx
    },
    { String paramName, def val, def realVal ->
        Storage.instance.updateTree([tsp: [initial: [(paramName): val]]])
    },
    { Map<Object, List> results ->
        results.keySet().max { k ->
            def ctx =  results[k].max { Context c ->
                c.globalBest.evaluate(c)
            }
            ctx.globalBest.evaluate(ctx)
        }
    }
).run()

//rastrigin
new Explore(
    "initial_rastrigin",
    5, 3,
    [ "popSize", "mixinFactor", "probs", "maxGen", "natSel"],
    [
        popSize: [ 10, 20, 50, 10 ],
        mixinFactor: [0.0, 0.1, 0.25, 0.5],
        probs: [0.6, 0.7, 0.8].collect { cp -> [0.1, 0.2, 0.3].collect { mp -> [cp, mp] } }.sum(),
        maxGen: [25, 50, 100, 200],
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
        def ctx = new Context()
        EAUtils.run(new RastriginSetup() {
            @Override
            int getDimensions() {
                return 10
            }

            @Override
            int getGenerations() {
                realConfig.maxGen
            }

            @Override
            double getCrossProb() {
                realConfig.probs[0]
            }

            @Override
            double getMutProb() {
                realConfig.probs[1]
            }

            @Override
            int getGendersCount() {
                1
            }

            @Override
            def getModel() {
                return null
            }

            @Override
            Context getContext(){
                ctx
            }

            @Override
            int getPopulationSize() {
                realConfig.popSize
            }

            @Override
            double getMixinFactor() {
                realConfig.mixinFactor
            }

            @Override
            NaturalSelectionOperator<Point> getNaturalSelection() {
                realConfig.natSel
            }

            @Override
            GenderSelectionOperator<Tour> getGenderSelection() {
                new NoGender<Point>(new RandomChoose<Point>())
            }
        })
        ctx
    },
    { String paramName, def val, def realVal ->
        Storage.instance.updateTree([rastrigin: [initial: [(paramName): val]]])
    },
    { Map<Object, List> results ->
        results.keySet().max { k ->
            def ctx =  results[k].max { Context c -> c.globalBest.evaluate(c) }
            ctx.globalBest.evaluate(ctx)
        }
    }
).run()

//knapsack
new Explore(
    "initial_knapsack",
    5, 3,
    [ "popSize", "mixinFactor", "probs", "maxGen", "natSel"],
    [
        popSize: [ 10, 20, 50, 10 ],
        mixinFactor: [0.0, 0.1, 0.25, 0.5],
        probs: [0.6, 0.7, 0.8].collect { cp -> [0.1, 0.2, 0.3].collect { mp -> [cp, mp] } }.sum(),
        maxGen: [25, 50, 100, 200],
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
        def ctx = ContextCategory.toContext(KnapsackResources.medium)
        EAUtils.run(new KnapsackSetup() {

            @Override
            int getGenerations() {
                realConfig.maxGen
            }

            @Override
            double getCrossProb() {
                realConfig.probs[0]
            }

            @Override
            double getMutProb() {
                realConfig.probs[1]
            }

            @Override
            int getGendersCount() {
                1
            }

            @Override
            def getModel() {
                return null
            }

            @Override
            Context getContext(){
                ctx
            }

            @Override
            int getPopulationSize() {
                realConfig.popSize
            }

            @Override
            double getMixinFactor() {
                realConfig.mixinFactor
            }

            @Override
            NaturalSelectionOperator<Point> getNaturalSelection() {
                realConfig.natSel
            }

            @Override
            GenderSelectionOperator<Tour> getGenderSelection() {
                new NoGender<Point>(new RandomChoose<Point>())
            }
        })
        ctx
    },
    { String paramName, def val, def realVal ->
        Storage.instance.updateTree([knapsack: [initial: [(paramName): val]]])
    },
    { Map<Object, List> results ->
        results.keySet().max { k ->
            def ctx =  results[k].max { Context c -> c.globalBest.evaluate(c) }
            ctx.globalBest.evaluate(ctx)
        }
    }
).run()