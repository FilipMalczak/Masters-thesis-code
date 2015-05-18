package com.github.filipmalczak.experiments

import com.github.filipmalczak.ea.utils.EAUtils

import static com.github.filipmalczak.experiments.DataSetConfig.getExecuteDataset

println "DoResearch"

//tsp
if (executeDataset.tsp)
    new DoResearchFixture(
        "tsp",
        5,
        EAUtils.&tspSetup
    ).run()

//knapsack
if (executeDataset.knapsack)
    new DoResearchFixture(
        "knapsack",
        5,
        EAUtils.&knapsackSetup
    ).run()