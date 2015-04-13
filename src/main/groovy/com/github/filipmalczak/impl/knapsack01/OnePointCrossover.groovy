package com.github.filipmalczak.impl.knapsack01

import com.github.filipmalczak.ea.operators.CrossoverOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.RandomUtils


class OnePointCrossover implements CrossoverOperator<BitVector>{
    /**
     * assumes 2 parents
     * @param parents
     * @param context
     * @return
     */
    @Override
    List<BitVector> crossOver(List<BitVector> parents, Context context) {
        def p1 = parents[0]
        def p2 = parents[1]
        int cutPoint = RandomUtils.random(p1.vector.size())
        int firstGenderIdx = RandomUtils.random(2)
        [
            new BitVector(
                p1.vector[0..cutPoint-1]+p2.vector[cutPoint..p2.vector.size()-1],
                parents[firstGenderIdx].gender
            ),
            new BitVector(
                p2.vector[0..cutPoint-1]+p1.vector[cutPoint..p1.vector.size()-1],
                parents[1-firstGenderIdx].gender
            )
        ]
    }
}
