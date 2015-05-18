package com.github.filipmalczak.ea

import com.github.filipmalczak.ea.alg.ContextHandler
import com.github.filipmalczak.ea.alg.GeneratePopulation
import com.github.filipmalczak.ea.alg.StopCondition
import com.github.filipmalczak.ea.operators.CrossoverOperator
import com.github.filipmalczak.ea.operators.GenderSelectionOperator
import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.ea.operators.NaturalSelectionOperator
import com.github.filipmalczak.ea.prob.CP
import com.github.filipmalczak.ea.prob.MP
import com.github.filipmalczak.ea.utils.EASetup
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
        log.debug "Problem: ${context.problemDefinition}"
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
            sexmissionProtocol(population, context)
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

    /**
     * This protects runs that use more than one gender from losing specimen of one sex.
     * If there are less than 5 specimen of some sex in population, max(ceiling(0.05*|population|), 5)
     * specimens of that sex are generated and added.
     *
     * Initially new specimens were introduced only when some sex was missing, but for example, when choosing
     * specimen of specific gender with tourney choose operator, and size of tourney is greater than number
     * of such specimens in population, we were getting into an infinite loop.
     * @param population Current population, extended in place, if needed
     * @param context Current run context
     */
    void sexmissionProtocol(List<S> population, Context<S> context){
        Map<Integer, Integer> missingSexes = [:]
        (0..(generatePopulation.genderCount-1)).each {
            missingSexes[it] = 5
        }
        def iterator = population.iterator()
        while (!missingSexes.isEmpty() && iterator.hasNext()){
            def next = iterator.next()
            if (missingSexes.containsKey(next.gender)) {
                missingSexes[next.gender]--
            }
        }
        def toGenerate = [ Math.ceil(populationSize*0.05) as int, 5 ].max()
        if (!missingSexes.isEmpty()) {
            log.warn("Jutro też wam uciekniemy!")
            log.debug("Sexmission protocol activated, generating $toGenerate specimens of each of sexes: $missingSexes")
        }
        missingSexes.keySet().each { int sex ->
            generatePopulation.generate(toGenerate, context).each {
                it.gender = sex
                population.add it
            }
        }
    }
}
