package com.github.filipmalczak.impl.distance

import com.github.filipmalczak.impl.choose_operator.MaxDiversityChoose
import com.github.filipmalczak.impl.rastrigin.Point


class SquareEuclideanDistance implements MaxDiversityChoose.Distance<Point>{
    @Override
    double calculate(Point t1, Point t2) {
        double out = 0.0
        //euclidean-like
        t1.coordinates.each {
            assert it!=null &&  t2 && "t1"
        }
        t2.coordinates.each {
            assert it!=null && t1 && "t2"
        }
        t1.coordinates.eachWithIndex { double entry, int i ->
            out += (entry - t2.coordinates[i])**2
        }
        out
    }
}
