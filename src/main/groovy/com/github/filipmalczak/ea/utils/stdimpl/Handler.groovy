package com.github.filipmalczak.ea.utils.stdimpl

import com.github.filipmalczak.ea.alg.ContextHandler
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.transform.Canonical
import groovy.util.logging.Slf4j

@Slf4j
@Canonical
class Handler<S extends Specimen> implements ContextHandler<S>{
    boolean calculateGenerationStats = true

    @Override
    void update(List<S> population, Context<S> context) {
        log.info("Generation ${context.generation}")
        def best = population.min { it.evaluate(context) }
        log.debug("Best: ${best.getPhenotype(context)} = ${best} ${best.evaluate(context)})")
        if (context.globalBest == null || context.globalBest.evaluate(context)> best.evaluate(context)) {
            context.globalBest = best
            log.debug("Became global best")
        }
        if (calculateGenerationStats){
            context.pushBest(best.evaluate(context))
            def worst = findWorst(population, context)
            log.debug("Worst: ${worst.getPhenotype(context)} = ${worst} ${worst.evaluate(context)})")
            context.pushWorst(worst.evaluate(context))
            def avg = avgEval(population, context)
            log.debug ("Average eval: $avg")
            context.pushAvg(avg)
            def var = variance(population, context, avg)
            log.debug("Eval variance: $var")
            context.pushVariance(var)
        }
    }

    static S findWorst(List<S> population, Context context){
        population.max { S ft -> ft.evaluate(context) }
    }

    static double avgEval(List<S> population, Context context){
        def collection  = population.collect { S ft -> ft.evaluate(context)/ population.size() }
        collection.sum()
    }

    static double variance(List<S> population, Context context, double avg){
        def collection = population.collect { S ft -> Math.abs(ft.evaluate(context) - avg)/ population.size() }
        collection.sum()
    }

    @Override
    void start(Context context) {
        log.debug("Start")
        context.startTime = new Date()
    }

    @Override
    void finish(Context context) {
        log.debug("Finish")
        context.endTime = new Date()
        log.info("Duration: ${context.duration}")
        log.info("Best: ${context.globalBest}")
        log.info("With phenotype: ${context.globalBest.getPhenotype(context)}")
        log.info("\tevaluating to:: ${context.globalBest.evaluate(context)}")
    }
}
