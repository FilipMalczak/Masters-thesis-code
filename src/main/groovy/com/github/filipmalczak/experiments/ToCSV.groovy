package com.github.filipmalczak.experiments

import com.github.filipmalczak.datasets.knapsack01.KnapsackResources
import com.github.filipmalczak.datasets.tsp.TSPResources
import com.github.filipmalczak.heuristics.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

println "ToCSV"
Logger log = LoggerFactory.getLogger(ToCSV)

Map<String, List<String>> paramNamesForExperiments = [
    "initial_tsp": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "initial_knapsack": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "initial_rastrigin": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "tweak_tsp": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "tweak_knapsack": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "tweak_rastrigin": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "small_initial_tsp": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"]
]

Map<String, Double> optimums = [
    "initial_tsp": TSPResources.sahara.bound,
    "initial_knapsack": KnapsackResources.medium.optimum,
    "initial_rastrigin": 0,
    "tweak_tsp": TSPResources.sahara.bound,
    "tweak_knapsack": KnapsackResources.medium.optimum,
    "tweak_rastrigin": 0,
]

def storage = Storage.instance

storage.init()
def tree = storage.treeSnapshot
storage.eachExperiment { String name, File dir ->
    def names = paramNamesForExperiments[name]
    if (names) {
        def f = storage.summaryFile(name + ".csv")
        def txt = (names + ["iteration", "eval", "optimum", "duration", "bestPhenotype"]).join(";") << "\n"
        storage.eachResult(name) { List<String> params, Context ctx ->
            txt << (params + ["${ctx.globalBest.evaluate(ctx)}", "${optimums[name]}", "${ctx.duration}", "${ctx.globalBest.getPhenotype(ctx)}"]).join(";") << "\n"
        }
        f.text = txt.toString()
    }
}
