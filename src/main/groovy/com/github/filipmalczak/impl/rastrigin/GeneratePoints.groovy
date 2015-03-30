package com.github.filipmalczak.impl.rastrigin

import com.github.filipmalczak.ea.alg.AbstractGeneratePopulation
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.RandomUtils

import groovy.transform.Canonical

@Canonical
class GeneratePoints extends AbstractGeneratePopulation<Point> {
    int dimensions

    @Override
    Point generateWithoutGender(Context<Point> context) {
        assert dimensions>1
        def coords = []
        dimensions.times {
            coords << RandomUtils.random(-5.12, 5.12)
        }
        new Point(coords)
    }
}
