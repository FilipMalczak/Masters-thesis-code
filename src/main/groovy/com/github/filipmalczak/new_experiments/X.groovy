package com.github.filipmalczak.new_experiments

import can.i.has.utils.IterUtils
import com.github.filipmalczak.datasets.knapsack01.KnapsackResources
import com.github.filipmalczak.datasets.tsp.TSPResources
import com.github.filipmalczak.experiments.Storage
import com.github.filipmalczak.new_experiments.aspects.ContextResult

import groovy.json.JsonBuilder

def original = new File("/home/phill/repos/groovy/Mgr/storage/results/initial_knapsack")
def target = new File("/home/phill/repos/groovy/Mgr/storage/results/new_initial_knapsack")
target.mkdirs()
original.eachFile { File f ->
    def params = f.name.replaceAll("[.]result", "").split("_")
    def paramMap = [:]
    IterUtils.zipEach([ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"], params as List) { x, y ->
        paramMap[x] = y
    }
    def ctx = Storage.deserialize(f.bytes)
    def res = new ContextResult(ctx, paramMap, KnapsackResources.medium.optimum)
    new File(target, f.name).bytes = Storage.serialize(res)
}

//def r = Storage.deserialize(new File("/home/phill/repos/groovy/Mgr/storage/results/new_initial_knapsack/10_0.0_0.6_0.1_25_roulette_0.result").bytes)
//println new JsonBuilder(r).toPrettyString()