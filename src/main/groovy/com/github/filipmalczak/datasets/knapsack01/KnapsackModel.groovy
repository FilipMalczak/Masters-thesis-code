package com.github.filipmalczak.datasets.knapsack01

import can.i.has.utils.Pair

import groovy.transform.Canonical

@Canonical
class KnapsackModel {
    int maxSize
    List<Pair<Integer, Integer>> data

    double optimum
}
