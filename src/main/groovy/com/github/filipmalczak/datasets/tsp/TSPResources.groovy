package com.github.filipmalczak.datasets.tsp

class TSPResources {
    static TSPModel xqf131
    static TSPModel sahara

    static {
        def loader = new TSPLoader()
        xqf131 = new TSPModel(564, loader.loadPoints("xqf131.tsp"))
        sahara = new TSPModel(27603, loader.loadPoints("sahara.tsp"))
    }

}
