package com.github.filipmalczak.experiments

import com.github.filipmalczak.datasets.ContextCategory
import com.github.filipmalczak.datasets.knapsack01.KnapsackResources
import com.github.filipmalczak.heuristics.Context

println KnapsackResources.small
println ContextCategory.toContext()


//
//import com.github.filipmalczak.heuristics.Context
//import com.github.filipmalczak.impl.rastrigin.Point
//
////def ctx = new Context()
////([
////    [-5.12]*20,
//    [0]*5+[-5.12]*15,
//    [0]*10+[-5.12]*10,
//    [0]*20
//]+(2..10).collect{
//    [-5.12/it]*20
//}).each { List<Double> coords ->
//    def p = new Point(coords, 0)
//    println "-------------------------"
//    println "eval:   ${p.evaluate(ctx)}"
//    println "length: ${p.vectorLength}"
//    println "coords: ${p.coordinates}"
//}
//
//
