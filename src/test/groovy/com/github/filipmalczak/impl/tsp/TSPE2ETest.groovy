package com.github.filipmalczak.impl.tsp

import com.github.filipmalczak.datasets.tsp.TSPResources
import com.github.filipmalczak.ea.utils.EAUtils
import com.github.filipmalczak.impl.AbstractE2ESetup
import com.github.filipmalczak.impl.AbstractTestSetup

class TSPE2ETest extends GroovyTestCase {
    int repeat = 5

    static abstract class TestSetup extends TSPSetup implements AbstractTestSetup, AbstractE2ESetup {}


    void testSahara(){
        repeat.times {
            EAUtils.run(
                new TestSetup(){
                    @Override
                    def getModel() {
                        TSPResources.sahara
                    }
                }
            )
        }
    }

    void testXqf131(){
        repeat.times {
            EAUtils.run(
                new TestSetup(){
                    @Override
                    def getModel() {
                        TSPResources.xqf131
                    }
                }
            )
        }
    }
}
