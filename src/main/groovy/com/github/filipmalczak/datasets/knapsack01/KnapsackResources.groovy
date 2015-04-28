package com.github.filipmalczak.datasets.knapsack01

import com.github.filipmalczak.datasets.ContextCategory
import com.github.filipmalczak.impl.knapsack01.BitVector


class KnapsackResources {
    static KnapsackModel small
    static KnapsackModel medium
    static KnapsackModel p01

    static {
        def loader = new KnapsackLoader()
        medium = loader.loadData("medium_knapsack.txt")
        medium.optimum = -1173
        p01 = loader.loadData("p01_knapsack.txt")
        p01.optimum = new BitVector([1,1,1,1,0,1,0,0,0,0].collect {it as Boolean},
            0).evaluate(ContextCategory.toContext(p01))
        small = loader.loadData("small.txt")
        small.optimum = -1149
    }
}
