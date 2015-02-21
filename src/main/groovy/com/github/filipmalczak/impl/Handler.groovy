package com.github.filipmalczak.impl

import com.github.filipmalczak.ga.alg.ContextHandler
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

import groovy.util.logging.Slf4j

@Slf4j
class Handler<S extends Specimen> implements ContextHandler<S>{
    @Override
    void update(List<S> population, Context context) {
        log.debug("Generation ${context.generation}")
        def best = population.max { it.evaluate(context) }
        log.debug("Best: $best (${best.evaluate(context)})")
        if (context.globalBest == null || context.globalBest.evaluate(context)< best.evaluate(context)) {
            context.globalBest = best
            log.debug("Became global best")
        }
//        def worst = population.min { it.evaluate(context) }

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
    }
}
