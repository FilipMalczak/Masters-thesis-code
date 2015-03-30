package com.github.filipmalczak.impl.tsp

import com.github.filipmalczak.datasets.ContextCategory
import com.github.filipmalczak.datasets.TSPTestResources
import com.github.filipmalczak.ea.EvolutionaryAlgorithm
import com.github.filipmalczak.impl.ConstCP
import com.github.filipmalczak.impl.ConstMP
import com.github.filipmalczak.impl.Handler
import com.github.filipmalczak.impl.NoGender
import com.github.filipmalczak.impl.Stop
import com.github.filipmalczak.impl.TourneyNaturalSelection


class TSPE2ETest extends GroovyTestCase {
    int repeat = 5
    def popSize = 20
    def mixinFactor = 0.25
    def handler = new Handler()
    def generate = new GenerateTours()

    def stop = new Stop(30)
    def cross = new MergeCrossover()
    def mutate = new SwapMutation()
    def naturalSelection = new TourneyNaturalSelection<Tour>(2)
    def cp = new ConstCP<Tour>(0.7)
    def mp = new ConstMP<Tour>(0.2)

    void testRunning(){
        use(ContextCategory) {
            repeat.times {
                new EvolutionaryAlgorithm<Tour>(
                    popSize, mixinFactor, handler, generate, stop, cross, mutate, naturalSelection,
                    new NoGender<Tour>(),
                    cp, mp
                ).doRun(TSPTestResources.sahara.toContext())
            }
        }
    }
}
