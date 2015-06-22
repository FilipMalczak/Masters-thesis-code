package com.github.filipmalczak.experiments.utils.formatting

import com.github.filipmalczak.datasets.knapsack01.KnapsackResources
import com.github.filipmalczak.datasets.tsp.TSPResources
import com.github.filipmalczak.experiments.utils.Storage
import com.github.filipmalczak.heuristics.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

println "ToCSV"
Logger log = LoggerFactory.getLogger(ToCSV)

Map<String, List<String>> paramNamesForExperiments = [
    "tsp_initial": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "knapsack_initial": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "tsp_tweak": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "knapsack_tweak": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"]
]

Map<String, Double> optimums = [
    "tsp_initial": TSPResources.sahara.bound,
    "knapsack_initial": KnapsackResources.medium.optimum,
    "tsp_tweak": TSPResources.sahara.bound,
    "knapsack_tweak": KnapsackResources.medium.optimum
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
