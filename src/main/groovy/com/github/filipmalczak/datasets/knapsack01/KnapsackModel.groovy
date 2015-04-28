package com.github.filipmalczak.datasets.knapsack01

import com.github.filipmalczak.utils.Pair

import groovy.transform.Canonical
import groovy.transform.Memoized

@Canonical
class KnapsackModel implements Serializable{
    int maxSize
    List<Pair<Integer, Integer>> data

    double optimum

    @Lazy totalSize = calculateTotalSize(data)
    @Lazy maxOverload = totalSize - maxSize

    static int calculateTotalSize(List<Pair<Integer, Integer>> data){
        data.sum { it.second }
    }
}
