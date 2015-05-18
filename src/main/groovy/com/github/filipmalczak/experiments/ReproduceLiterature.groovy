package com.github.filipmalczak.experiments

import groovyx.gpars.GParsPool

import com.github.filipmalczak.experiments.ReproduceLiteratureFixtures as Fixtures

int poolSize = 9
def gpool = new GParsPool()
def pool = gpool.createPool(poolSize)
gpool.withExistingPool(pool) {
    [
        Fixtures.&doSexualGA, Fixtures.&doGGA
    ].eachParallel { Closure fixture ->
        gpool.withExistingPool(pool) {
            ["tsp", "knapsack"].eachParallel { String modelName ->
                fixture(modelName, gpool, pool)
            }
        }
    }
}
