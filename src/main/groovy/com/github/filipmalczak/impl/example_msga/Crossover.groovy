package com.github.filipmalczak.impl.example_msga

import com.github.filipmalczak.ea.operators.CrossoverOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.RandomUtils

import static com.github.filipmalczak.utils.RandomUtils.*

class Crossover implements CrossoverOperator<Point>{
    @Override
    List<Point> crossOver(List<Point> parents, Context context) {
        def sumX = 0.0
        def sumY = 0.0
        def weightSum = 0.0
        def weights = parents.collect { def w = random(0.0, 1.0); weightSum += w; return w }
        weights = weights.collect { it/weightSum }
        parents.eachWithIndex { Point p, int i ->
            sumX += weights[i]*p.x
            sumY += weights[i]*p.y
        }
        [ new Point(sumX/parents.size(), sumY/parents.size(), RandomUtils.random(parents).gender) ]
    }
}
