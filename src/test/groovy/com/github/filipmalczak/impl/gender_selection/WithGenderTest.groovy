package com.github.filipmalczak.impl.gender_selection

import com.github.filipmalczak.ea.operators.GenderSelectionOperator
import com.github.filipmalczak.ea.operators.NaturalSelectionOperator
import com.github.filipmalczak.ea.utils.EAUtils
import com.github.filipmalczak.impl.AbstractTestSetup
import com.github.filipmalczak.impl.choose_operator.ChooseOperator
import com.github.filipmalczak.impl.choose_operator.MaxDiversityChoose
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.choose_operator.RankRouletteChoose
import com.github.filipmalczak.impl.choose_operator.TourneyChoose
import com.github.filipmalczak.impl.distance.SquareEuclideanDistance
import com.github.filipmalczak.impl.natural_selection.NaturalSelection
import com.github.filipmalczak.impl.rastrigin.Point
import com.github.filipmalczak.impl.rastrigin.RastriginSetup
import com.github.filipmalczak.utils.RandomUtils

import groovy.util.logging.Slf4j

@Slf4j("logger")
class WithGenderTest extends GroovyTestCase {
    int repeat = 20
    def chooseOperators = [
        new RandomChoose<Point>(),
        new RankRouletteChoose<Point>(),
        new TourneyChoose<Point>(2)
    ]
    def diversityChoose = new MaxDiversityChoose<Point>(new SquareEuclideanDistance())

    static abstract class TestSetup extends RastriginSetup implements AbstractTestSetup {

        boolean forceDychotomy(){
            return true
        }

        @Override
        int getDimensions() {
            2
        }

        @Override
        int getGendersCount() {
            2
        }

        @Override
        def getModel() {
            return null
        }

        abstract ChooseOperator<Point> getChooseXOperator()
        abstract ChooseOperator<Point> getChooseYOperator()

        @Override
        NaturalSelectionOperator<Point> getNaturalSelection() {
            new NaturalSelection<Point>(new RandomChoose<Point>())
        }

        @Override
        GenderSelectionOperator<Point> getGenderSelection() {
            new WithGender<Point>([(0): chooseXOperator, (1): chooseYOperator], forceDychotomy())
        }
    }




    void testGenderWithDychotomy(){
        RandomUtils.init(1000)
        chooseOperators.each { ChooseOperator<Point> x ->
            chooseOperators+[diversityChoose].each { ChooseOperator<Point> y ->
                logger.info "Testing x: $x, y: $y"
                repeat.times {
                    EAUtils.run(new TestSetup() {

                        @Override
                        ChooseOperator<Point> getChooseXOperator() {
                            x
                        }

                        @Override
                        ChooseOperator<Point> getChooseYOperator() {
                            y
                        }
                    })
                }
            }
        }
    }

    void testGenderWithoutDychotomy(){
        RandomUtils.init(1000)
        chooseOperators.each { ChooseOperator<Point> x ->
            chooseOperators+[diversityChoose].each { ChooseOperator<Point> y ->
                logger.info "Testing x: $x, y: $y"
                repeat.times {
                    EAUtils.run(new TestSetup() {

                        @Override
                        boolean forceDychotomy() {
                            return false
                        }

                        @Override
                        ChooseOperator<Point> getChooseXOperator() {
                            x
                        }

                        @Override
                        ChooseOperator<Point> getChooseYOperator() {
                            y
                        }
                    })
                }
            }
        }
    }
}
