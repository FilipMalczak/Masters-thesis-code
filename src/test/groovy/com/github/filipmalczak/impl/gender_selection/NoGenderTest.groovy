package com.github.filipmalczak.impl.gender_selection

import com.github.filipmalczak.ea.operators.GenderSelectionOperator
import com.github.filipmalczak.ea.operators.NaturalSelectionOperator
import com.github.filipmalczak.ea.utils.EAUtils
import com.github.filipmalczak.impl.AbstractTestSetup
import com.github.filipmalczak.impl.choose_operator.ChooseOperator
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.choose_operator.RankRouletteChoose
import com.github.filipmalczak.impl.choose_operator.TourneyChoose
import com.github.filipmalczak.impl.natural_selection.NaturalSelection
import com.github.filipmalczak.impl.rastrigin.Point
import com.github.filipmalczak.impl.rastrigin.RastriginSetup

import groovy.util.logging.Slf4j

@Slf4j("logger")
class NoGenderTest extends GroovyTestCase {
    int repeat = 5
    def chooseOperators = [
        new RandomChoose<Point>(),
        new RankRouletteChoose<Point>(),
        new TourneyChoose<Point>(2)
    ]

    static abstract class TestSetup extends RastriginSetup implements AbstractTestSetup {

        @Override
        int getDimensions() {
            2
        }

        @Override
        int getGendersCount() {
            1
        }

        @Override
        def getModel() {
            return null
        }

        abstract ChooseOperator<Point> getChooseOperator()

        @Override
        NaturalSelectionOperator<Point> getNaturalSelection() {
            new NaturalSelection<Point>(new RandomChoose<Point>())
        }

        @Override
        GenderSelectionOperator<Point> getGenderSelection() {
            new NoGender<Point>(chooseOperator)
        }
    }

    void testNoGender(){
        chooseOperators.each { ChooseOperator<Point> tested ->
            logger.info "Testing $tested"
            repeat.times {
                EAUtils.run(new TestSetup() {
                    @Override
                    ChooseOperator<Point> getChooseOperator() {
                        tested
                    }
                })
            }
        }
    }
}
