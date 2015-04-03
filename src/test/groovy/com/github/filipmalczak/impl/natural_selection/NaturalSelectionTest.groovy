package com.github.filipmalczak.impl.natural_selection

import com.github.filipmalczak.datasets.ContextCategory
import com.github.filipmalczak.datasets.TSPTestResources
import com.github.filipmalczak.ea.EvolutionaryAlgorithm
import com.github.filipmalczak.impl.ConstCP
import com.github.filipmalczak.impl.ConstMP
import com.github.filipmalczak.impl.Handler
import com.github.filipmalczak.impl.Stop
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.choose_operator.RankRouletteChoose
import com.github.filipmalczak.impl.choose_operator.TourneyChoose
import com.github.filipmalczak.impl.gender_selection.NoGender
import com.github.filipmalczak.impl.gender_selection.WithGender
import com.github.filipmalczak.impl.rastrigin.Point
import com.github.filipmalczak.impl.tsp.GenerateTours
import com.github.filipmalczak.impl.tsp.MergeCrossover
import com.github.filipmalczak.impl.tsp.SwapMutation
import com.github.filipmalczak.impl.tsp.Tour


class NaturalSelectionTest extends GroovyTestCase {
    int repeat = 5
    def popSize = 20
    def mixinFactor = 0.25
    def handler = new Handler()
    def generate = new GenerateTours()

    def stop = new Stop(30)
    def cross = new MergeCrossover()
    def mutate = new SwapMutation()
    def naturalSelection = new NaturalSelection<Tour>()
    def cp = new ConstCP<Tour>(0.7)
    def mp = new ConstMP<Tour>(0.2)

    def chooseOperators = [
        new RandomChoose<Tour>(),
        new RankRouletteChoose<Tour>(),
        new TourneyChoose<Tour>(2)
    ]

    void testSelections(){
        use(ContextCategory) {
            chooseOperators.each {
                naturalSelection.chooseOperator = it
                repeat.times {
                    new EvolutionaryAlgorithm<Tour>(
                        popSize, mixinFactor, handler, generate, stop, cross, mutate, naturalSelection,
                        new NoGender<Point>(new RandomChoose<Point>()),
                        cp, mp
                    ).doRun(TSPTestResources.sahara.toContext())
                }
            }
        }
    }
}
