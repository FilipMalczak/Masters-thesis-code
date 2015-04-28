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
            Storage.instance.updateTree([tsp: [tweak: [(paramName): val]]])
        },
        { Map<Object, List> results ->
            results.keySet().min { k ->
                def ctx =  results[k].min { Context c -> c.globalBest.evaluate(c) }
                ctx.globalBest.evaluate(ctx)
            }
        },
        [ "initial_tsp" ]
    ).run()

//rastrigin
if (executeDataset.rastrigin)
    new Explore(
        "tweak_rastrigin",
        3, 2,
        [ "popSize", "mixinFactor", "probs", "maxGen", "natSel"],
        tweakParamSets("rastrigin"),
        vals,
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
            Storage.instance.updateTree([rastrigin: [tweak: [(paramName): val]]])
        },
        { Map<Object, List> results ->
            results.keySet().min { k ->
                def ctx =  results[k].min { Context c -> c.globalBest.evaluate(c) }
                ctx.globalBest.evaluate(ctx)
            }
        },
        [ "initial_rastrigin" ]
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
            Storage.instance.updateTree([knapsack: [tweak: [(paramName): val]]])
        },
        { Map<Object, List> results ->
            results.keySet().min { k ->
                def ctx =  results[k].min { Context c -> c.globalBest.evaluate(c) }
                ctx.globalBest.evaluate(ctx)
            }
        },
        [ "initial_knapsack" ]
    ).run()