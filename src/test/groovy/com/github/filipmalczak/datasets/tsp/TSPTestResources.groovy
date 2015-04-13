package com.github.filipmalczak.datasets.tsp

import com.github.filipmalczak.datasets.tsp.TSPLoader
import com.github.filipmalczak.datasets.tsp.TSPModel

class TSPTestResources {
    static TSPModel sahara

    static {
        def loader = new TSPLoader()
        sahara = new TSPModel(27603, loader.loadPoints("sahara.tsp"))
    }
}
