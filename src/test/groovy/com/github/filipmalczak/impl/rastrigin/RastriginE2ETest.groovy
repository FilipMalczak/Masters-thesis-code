package com.github.filipmalczak.impl.rastrigin

import com.github.filipmalczak.datasets.ContextCategory
import com.github.filipmalczak.ea.EvolutionaryAlgorithm
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.impl.ConstCP
import com.github.filipmalczak.impl.ConstMP
import com.github.filipmalczak.impl.Handler
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.choose_operator.TourneyChoose
import com.github.filipmalczak.impl.gender_selection.NoGender
import com.github.filipmalczak.impl.gender_selection.WithGender
import com.github.filipmalczak.impl.Stop
import com.github.filipmalczak.impl.natural_selection.NaturalSelection
import com.github.filipmalczak.impl.tsp.Tour


class RastriginE2ETest extends GroovyTestCase {
    int repeat = 5
    def popSize = 20
    def mixinFactor = 0.25
    def handler = new Handler()
    def generate = new GeneratePoints(5)
    def stop = new Stop(30)
    def cross = new AvgCrossover()
    def multMutate = new MultiplyMutation()
    def swapMutate = new SwapMutation()
    def naturalSelection = new NaturalSelection<Tour>(new TourneyChoose<Tour>(2))
    def cp = new ConstCP<Tour>(0.7)
    def mp = new ConstMP<Tour>(0.2)

    void testRunningWithMultiplyMutate(){
        //note: seems to be better
        use(ContextCategory) {
            repeat.times {
                new EvolutionaryAlgorithm<Point>(
                    popSize, mixinFactor, handler, generate, stop, cross, multMutate, naturalSelection,
                    new NoGender<Point>(new RandomChoose<Point>()),
                    cp, mp
                ).doRun(new Context())
            }
        }
    }

    void testRunningWithSwapMutate(){
        use(ContextCategory) {
            repeat.times {
                new EvolutionaryAlgorithm<Point>(
                    popSize, mixinFactor, handler, generate, stop, cross, swapMutate, naturalSelection,
                    new WithGender<Point>(),
                    cp, mp
                ).doRun(new Context())
            }
        }
    }
}
