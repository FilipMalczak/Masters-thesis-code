package com.github.filipmalczak.experiments

import com.github.filipmalczak.datasets.knapsack01.KnapsackResources
import com.github.filipmalczak.datasets.tsp.TSPResources
import com.github.filipmalczak.heuristics.Context

Map<String, List<String>> paramNamesForExperiments = [
    "initial_tsp": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "initial_knapsack": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
    "initial_rastrigin": [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"]
]

Map<String, Double> optimums = [
    "initial_tsp": TSPResources.xqf131.bound,
    "initial_knapsack": KnapsackResources.medium.optimum,
    "initial_rastrigin": 0
]

def storage = Storage.instance

storage.init()
storage.eachExperiment { String name, File dir ->
    def txt = (paramNamesForExperiments[name] + ["iteration", "eval", "optimum", "duration" ]).join(";") << "\n"
    storage.eachResult(name) { List<String> params, Context ctx ->
        txt << (params + ["${ctx.globalBest.evaluate(ctx)}", "${optimums[name]}", "${ctx.duration}" ]).join(";") << "\n"
    }
    storage.summaryFile(name+".csv").text = txt.toString()
}

//import groovy.transform.Canonical
//
//
//
//@Canonical
//class ToCSV implements Runnable{
//    List<String> paramNames
//
//
//    void run(){
//        println((paramNames+["iteration", "eval", "duration"]).join(";"))
//        Storage.instance.eachResult(experimentName) { List<String> params, def result ->
//            println((params+["${eval.call(result)}", ]).join(";"))
//        }
//    }
//
//    static void main(String... args){
//        Storage.instance.init()
//        new ToCSV(
//            "initial_rastrigin",
//            [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
//            { Context ctx ->
//                ctx.globalBest.evaluate(ctx)
//            }
//        ).run()
//    }
//}
