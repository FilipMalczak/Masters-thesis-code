package com.github.filipmalczak.impl.example_msga

import com.github.filipmalczak.ga.EvolutionaryAlgorithm
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.impl.ConstCP
import com.github.filipmalczak.impl.ConstMP
import com.github.filipmalczak.impl.Handler
import com.github.filipmalczak.impl.Harem
import com.github.filipmalczak.impl.TourneyNaturalSelection
import com.github.filipmalczak.impl.NoGender
import com.github.filipmalczak.impl.Stop

def popSize = 20
def mixinFactor = 0.25
def handler = new Handler()
def generate = new Generate()
def stop = new Stop(10)
def cross = new Crossover()
def mutate = new Mutation()
def naturalSelection = new TourneyNaturalSelection<Point>(2)
def cp = new ConstCP<Point>(0.7)
def mp = new ConstMP<Point>(0.2)

def repeat = 20

println "gender;best;repr"

// no gender
repeat.times {
    def ctx = new Context<Point>()
    def ea = new EvolutionaryAlgorithm<Point>(
        popSize, mixinFactor, handler, generate, stop, cross, mutate, naturalSelection,
        new NoGender<Point>(),
        cp, mp
    )
    ea.doRun(ctx)
    println("no;${ctx.globalBest.evaluate(ctx)};${ctx.globalBest}")
}


// harem
repeat.times {
    def ctx = new Context<Point>()
    def ea = new EvolutionaryAlgorithm<Point>(
        popSize, mixinFactor, handler, generate, stop, cross, mutate, naturalSelection,
        new Harem<Point>(0, 0.25),
        cp, mp
    )
    ea.doRun(ctx)
    println("harem;${ctx.globalBest.evaluate(ctx)};${ctx.globalBest}")
}