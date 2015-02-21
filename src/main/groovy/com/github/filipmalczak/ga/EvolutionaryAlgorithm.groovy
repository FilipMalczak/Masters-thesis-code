package com.github.filipmalczak.ga

import com.github.filipmalczak.ga.alg.ContextHandler
import com.github.filipmalczak.ga.alg.GeneratePopulation
import com.github.filipmalczak.ga.alg.StopCondition
import com.github.filipmalczak.ga.operators.CrossoverOperator
import com.github.filipmalczak.ga.operators.GenderSelectionOperator
import com.github.filipmalczak.ga.operators.MutationOperator
import com.github.filipmalczak.ga.operators.NaturalSelectionOperator
import com.github.filipmalczak.ga.prob.CP
import com.github.filipmalczak.ga.prob.MP
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical
import groovy.util.logging.Slf4j

import static com.github.filipmalczak.utils.RandomUtils.happens

@Canonical
@Slf4j
class EvolutionaryAlgorithm<S extends Specimen> {
    int populationSize
    double mixinFactor

    ContextHandler<S> contextHandler
    GeneratePopulation<S> generatePopulation
    StopCondition<S> stop

    CrossoverOperator<S> crossover
    MutationOperator<S> mutation
    NaturalSelectionOperator<S> naturalSelection
    GenderSelectionOperator<S> genderSelection

    CP<S> cp
    MP<S> mp



    /**
     * Run genetic algorithm parametrized with attributes of this instance.
     * @param callback Closure taking (List<S> population, int generation, Map context) called at the beginning of each
     * population (after context handling/update). Useful for tracking progress or generating CSV data.
     * @return Last population, sorted (ascending) by evaluation.
     */
    List<S> doRun(Context context) {
        List<S> population = generatePopulation.generate(populationSize, context)
        context.generation = 0

        contextHandler.start(context)

        while (!stop.shouldStop(population, context)) {
            def mutProb = mp.getMutationProbability(population, context)
            def crossProb = cp.getCrossoverProbability(population, context)
            context.mutProb = mutProb
            context.crossProb = crossProb
            log.debug("CP = $crossProb, MP = $mutProb")
            contextHandler.update(population, context)
            log.debug("Mixin addition")
            population += generatePopulation.generate((int)Math.ceil(mixinFactor*populationSize), context)
            log.debug("Done")
            log.debug("Gender selection")
            List<List<S>> parentSets = genderSelection.selectParentSets(population, populationSize, context)
            log.debug("Done")
            log.debug("Crossing over")
            parentSets.each { List<S> parents ->
//                assert parents.collect { it.gender }.toSet().size() == parents.size()
                population += crossover.crossOver(parents, context)
            }
            log.debug("Done")
            log.debug("Mutating")
            population = population.collect { S s ->
                happens(mutProb) ?
                    mutation.mutate(s, context)
                    : [s]
            }.flatten()
            log.debug("Done")
            log.debug("Natural selection")
            population = naturalSelection.selectNewPopulation(populationSize, population, context)
            log.debug("Done")
            context.generation++
        }
        contextHandler.finish(context)
        log.debug("Finish")
        return population
    }


}
