package com.github.filipmalczak.impl.rastrigin

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical
import groovy.transform.Memoized

import static java.lang.Math.PI
import static java.lang.Math.cos

@Canonical
class Point implements Specimen{

    List<Double> coordinates
    int gender = 0

    @Override
    double evaluate(Context context) {
        calculateRastrigin(coordinates)
    }

    protected static double doublePi = 2*PI

    @Memoized(maxCacheSize = 5000)//todo: tweak this for efficiency if it goes too slow
    protected static double calculateRastrigin(List<Double> coords){
        10*coords.size() + coords.sum { it*it + 10*cos(it*doublePi) }
    }

    @Override
    Specimen copy() {
        new Point([*coordinates], gender)
    }
}
