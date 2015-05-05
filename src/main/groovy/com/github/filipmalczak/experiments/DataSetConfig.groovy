package com.github.filipmalczak.experiments


class DataSetConfig {
    static executeDataset = [
        tsp: true,
        rastrigin: false,
        knapsack: true
    ]

    static executeStage = [
        initial: true,
        tweak: true,
        csv: true
    ]
}
