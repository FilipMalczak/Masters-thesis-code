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
import com.github.filipmalczak.impl.natural_selection.NaturalSelection
import com.github.filipmalczak.impl.rastrigin.Point
import com.github.filipmalczak.impl.rastrigin.RastriginSetup

import groovy.util.logging.Slf4j

@Slf4j("logger")
class WithGenderTest extends GroovyTestCase {
    int repeat = 5
    def chooseOperators = [
        new RandomChoose<Point>(),
        new RankRouletteChoose<Point>(),
        new TourneyChoose<Point>(2)
    ]
    def diversityChoose = new MaxDiversityChoose<Point>(new MaxDiversityChoose.Distance<Point>(){

        @Override
        double calculate(Point t1, Point t2) {
            double out = 0.0
            //euclidean-like
            t1.coordinates.eachWithIndex { Comparable<Double> entry, int i ->
                out += (entry - t2.coordinates[i])**2
            }
        }
    })

    static abstract class TestSetup extends RastriginSetup implements AbstractTestSetup {

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
            new WithGender<Point>([(0): chooseXOperator, (1): chooseYOperator])
        }
    }




    void testGender(){
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
}
