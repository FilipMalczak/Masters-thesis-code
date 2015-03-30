package com.github.filipmalczak.datasets

import com.github.filipmalczak.datasets.tsp.TSPLoader
import com.github.filipmalczak.datasets.tsp.TSPModel
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.impl.tsp.Tour


class TSPTestResources {
    static TSPModel sahara

    static {
        def loader = new TSPLoader()
        sahara = new TSPModel(27603, loader.loadPoints("sahara.tsp"))
    }
}
