package com.github.filipmalczak.impl.rastrigin

import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.ea.utils.EAUtils
import com.github.filipmalczak.impl.AbstractE2ESetup
import com.github.filipmalczak.impl.AbstractTestSetup

class RastriginE2ETest extends GroovyTestCase {
    int repeat = 5
    def multMutate = new MultiplyMutation()
    def swapMutate = new SwapMutation()

    static abstract class TestSetup extends RastriginSetup implements AbstractTestSetup, AbstractE2ESetup{
        @Override
        int getDimensions() {
            5
        }

        @Override
        def getModel() {
            return null
        }
    }

    void testRunningWithMultiplyMutate(){
        //note: seems to be better
        repeat.times {
            EAUtils.run(new TestSetup() {
                @Override
                MutationOperator<Point> getMutation() {
                    multMutate
                }
            })
        }
    }


    void testRunningWithSwapMutate(){
        repeat.times {
            EAUtils.run(new TestSetup() {
                @Override
                MutationOperator<Point> getMutation() {
                    swapMutate
                }
            })
        }
    }
}
