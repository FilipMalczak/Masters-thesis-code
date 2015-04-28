package com.github.filipmalczak.impl.knapsack01

import com.github.filipmalczak.datasets.knapsack01.KnapsackResources
import com.github.filipmalczak.ea.utils.EAUtils
import com.github.filipmalczak.impl.AbstractE2ESetup
import com.github.filipmalczak.impl.AbstractTestSetup


class KnapsackE2ETest extends GroovyTestCase {
    int repeat = 5

    static abstract class TestSetup extends KnapsackSetup implements AbstractTestSetup, AbstractE2ESetup {}

    void testP01(){
        repeat.times {
            EAUtils.run(new TestSetup() {
                @Override
                def getModel() {
                    KnapsackResources.p01
                }
            })
        }
    }

    void testMedium(){
        repeat.times {
            EAUtils.run(new TestSetup() {
                @Override
                def getModel() {
                    KnapsackResources.medium
                }
            })
        }
    }
}
