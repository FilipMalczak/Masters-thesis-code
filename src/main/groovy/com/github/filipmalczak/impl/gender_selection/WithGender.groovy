package com.github.filipmalczak.impl.gender_selection

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.impl.choose_operator.ChooseOperator

import groovy.transform.Canonical
import groovy.util.logging.Slf4j

@Canonical
@Slf4j
class WithGender<S extends Specimen> extends AbstractGenderSelection<S>{
    Map<Integer, ChooseOperator<S>> chooseOperators
    boolean forceDychotomy = true

    @Override
    boolean usesGender() {
        forceDychotomy
    }

    @Override
    List<S> generateParentSet(List<S> population, Context context, Map<Integer, List<S>> grouped) {
        def parents = []
        def order = grouped.keySet().sort()
        order.each { gender ->
            def specimens = grouped[gender]
            log.debug "${grouped[gender].size()} specimens of gender $gender"
            // oooooh its baaaad luck to be youuuuuu...
            def chosenOne = chooseOperators[gender].chooseOne(specimens, context, parents)
            parents << chosenOne
        }
        try {
            assert parents.size() == chooseOperators.keySet().size()
        } catch (Throwable t){
            throw t
        }
        parents
    }

}
