package com.github.filipmalczak.datasets.tsp

import com.github.filipmalczak.heuristics.Context


class TSPResources {
    static TSPModel xqf131

    static {
        def loader = new TSPLoader()
        xqf131 = new TSPModel(564, loader.loadPoints("xqf131.tsp"))
    }

}
