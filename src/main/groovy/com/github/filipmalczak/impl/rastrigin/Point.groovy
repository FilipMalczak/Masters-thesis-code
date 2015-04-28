package com.github.filipmalczak.impl.rastrigin

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.impl.choose_operator.MaxDiversityChoose.Distance
import com.github.filipmalczak.impl.distance.SquareEuclideanDistance

import groovy.json.JsonBuilder
import groovy.transform.Canonical
import groovy.transform.Memoized

import static java.lang.Math.PI
import static java.lang.Math.cos

@Canonical
class Point implements Specimen{

    List<Double> coordinates
    int gender = 0

    static Distance<Point> distance = new SquareEuclideanDistance()

    static Map<Integer, Point> zero = [:].withDefault { int dim -> new Point([0.0]*dim) }


    @Override
    double evaluate(Context context) {
        calculateRastrigin(coordinates)
    }

    @Override
    def getPhenotype(Context context) {
        new JsonBuilder([eval: evaluate(context), dist: vectorLength]).toString()
    }
    protected static double doublePi = 2*PI

    @Memoized(maxCacheSize = 5000)//todo: tweak this for efficiency if it goes too slow
    protected static double calculateRastrigin(List<Double> coords){
        10*coords.size() + coords.sum { it*it - 10*cos(it*doublePi) }
    }

    @Override
    Specimen copy() {
        new Point([*coordinates], gender)
    }

    int getDimensions(){
        coordinates.size()
    }

    double getVectorLength(){
        distance.calculate(this, zero[dimensions])
    }
}
