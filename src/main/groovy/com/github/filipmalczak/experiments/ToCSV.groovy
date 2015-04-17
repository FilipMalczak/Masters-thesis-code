package com.github.filipmalczak.experiments

import com.github.filipmalczak.heuristics.Context

import groovy.transform.Canonical

@Canonical
class ToCSV implements Runnable{
    String experimentName
    List<String> paramNames
    Closure eval

    void run(){
        println((paramNames+["iteration", "eval"]).join(";"))
        Storage.instance.eachResult(experimentName) { List<String> params, def result ->
            println((params+["${eval.call(result)}"]).join(";"))
        }
    }

    static void main(String... args){
        Storage.instance.init()
        new ToCSV(
            "initial_rastrigin",
            [ "popSize", "mixinFactor", "cp", "mp", "maxGen", "natSel"],
            { Context ctx ->
                ctx.globalBest.evaluate(ctx)
            }
        ).run()
    }
}
