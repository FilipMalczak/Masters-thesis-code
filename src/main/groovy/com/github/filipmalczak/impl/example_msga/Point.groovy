package com.github.filipmalczak.impl.example_msga

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical

import static java.lang.Math.pow
import static java.lang.Math.sin

@Canonical
class Point implements Specimen{
    double x
    double y
    int gender

    static final int k = 20

    @Override
    double evaluate(Context context) {
        pow(x, 1.0/3.0)*sin(x) + pow(y, 1.0/3.0)*sin(y) + k
    }

    @Override
    Specimen copy() {
        new Point(x, y, gender)
    }
}
