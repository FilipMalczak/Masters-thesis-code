package com.github.filipmalczak.datasets.knapsack01

import com.github.filipmalczak.utils.Pair

import groovy.transform.Canonical

@Canonical
class KnapsackModel implements Serializable{
    int maxSize
    List<Pair<Integer, Integer>> data

    double optimum
}
