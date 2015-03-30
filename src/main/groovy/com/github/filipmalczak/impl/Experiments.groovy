package com.github.filipmalczak.impl

import com.github.filipmalczak.impl.example_msga.Generate
import com.github.filipmalczak.impl.example_msga.Point
import com.github.filipmalczak.impl.tsp.MergeCrossover
import com.github.filipmalczak.impl.tsp.SwapMutation

def popSize = 20
def mixinFactor = 0.25
def handler = new Handler()
def generate = new Generate()
def stop = new Stop(10)
def cross = new MergeCrossover()
def mutate = new SwapMutation()
def naturalSelection = new TourneyNaturalSelection<Point>(2)
def cp = new ConstCP<Point>(0.7)
def mp = new ConstMP<Point>(0.2)


public <K, V> Map<K, V> collectToMap(Iterable<K> keys, Closure<V> closure){
    Map<K, V> out = [:]
    keys.each { K k ->
        out[k] = closure.call(k)
    }
}



//(new GreX().eval {
//    workspace(root: "./workspace") {
//        experiments {
//            experiment(name: "fullSearch") {
//                experimentBody = {
//                    println it
//                }
//                config = fullSearch {
//                    params = ["popSize", "mixinFactor", "maxGen", "tourneySize", "cp", "mp", "iteration"]
//                    domains = [
//                        popSize    : [10, 20, 40],
//                        mixinFactor: [0.0, 0.25],
//                        maxGen     : [10, 20, 50, 100],
//                        tourneySize: [2],
//                        cp         : [0.7, 0.9],
//                        mp         : [0.2, 0.5],
//                        iteration  : (0..4)
//                    ]
//                    values = [
//                        cp    : { new ConstCP<Tour>(it) },
//                        mp    : { new ConstMP<Tour>(it) },
//                        maxGen: { new Stop(it) }
//                    ]
//                }
//                runner = singleThread()
//            }
//        }
//    }
//} as Workspace).performAll()
