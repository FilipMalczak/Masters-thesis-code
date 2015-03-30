package com.github.filipmalczak.impl.tsp

import com.github.filipmalczak.heuristics.Context


class SwapMutationTest extends GroovyTestCase {
    void testMutate() {
        def idxs = [1, 2, 3, 4]
        def mutant = new SwapMutation().mutate(new Tour(idxs), new Context())[0]
        assert mutant.pointNumbers.size() == 4
        def matches = 0
        Set notMatchedOriginal = [].toSet()
        Set notMatchedMutant = [].toSet()
        idxs.eachWithIndex { int idx, int i ->
            if (idx == mutant.pointNumbers[i])
                matches ++
            else {
                notMatchedOriginal.add(idx)
                notMatchedMutant.add(mutant.pointNumbers[i])
            }
        }
        assert matches == mutant.pointNumbers.size() - 2
        assert notMatchedOriginal == notMatchedMutant
    }
}
