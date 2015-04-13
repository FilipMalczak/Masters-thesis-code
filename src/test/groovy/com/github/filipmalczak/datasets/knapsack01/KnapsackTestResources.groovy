package com.github.filipmalczak.datasets.knapsack01


class KnapsackTestResources {
    static KnapsackModel p01

    static {
        p01 = new KnapsackLoader().loadData("p01_knapsack.txt")
        p01.optimum = 1.0/309
    }
}
