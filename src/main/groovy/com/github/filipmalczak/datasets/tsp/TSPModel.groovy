package com.github.filipmalczak.datasets.tsp

import can.i.has.utils.Pair

import groovy.transform.Canonical
import groovy.transform.Memoized

@Canonical
class TSPModel {
    Number bound
    List<Pair<Double , Double >> points

    Number distanceSquared(int point1, int point2){
        def p1 = points[point1]
        def p2 = points[point2]
        def dx = p1.first - p2.first
        def dy = p1.second - p2.second
        dx*dx + dy*dy
    }

    @Memoized
    Number distance(int point1, int point2){
        Math.sqrt(distanceSquared(point1, point2))
    }
}
