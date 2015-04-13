package com.github.filipmalczak.datasets.knapsack01


class KnapsackResources {
    static KnapsackModel medium

    static {
        medium = new KnapsackLoader().loadData("medium_knapsack.txt")
        medium.optimum = 1.0/309
    }
}
