package com.github.filipmalczak.datasets.knapsack01

import com.github.filipmalczak.utils.Pair


class KnapsackLoaderTest extends GroovyTestCase {
    KnapsackModel expected = new KnapsackModel(
        165,
        [
            new Pair<Integer, Integer>(92, 23),
            new Pair<Integer, Integer>(57, 31),
            new Pair<Integer, Integer>(49, 29),
            new Pair<Integer, Integer>(68, 44),
            new Pair<Integer, Integer>(60, 53),
            new Pair<Integer, Integer>(43, 38),
            new Pair<Integer, Integer>(67, 63),
            new Pair<Integer, Integer>(84, 85),
            new Pair<Integer, Integer>(87, 89),
            new Pair<Integer, Integer>(72, 82)
        ],
        1.0/309
    )

    void testLoading(){
        assertEquals(expected, KnapsackResources.p01)
    }
}
