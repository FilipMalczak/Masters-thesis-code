package com.github.filipmalczak.datasets

import can.i.has.utils.Pair
import com.github.filipmalczak.datasets.tsp.TSPLoader


class TSPLoaderTest extends GroovyTestCase {
    List<Pair<Number, Number>> expected

    void setUp(){
        expected = [
            new Pair<Number, Number>(20833.3333,17100.0000),
            new Pair<Number, Number>(20900.0000,17066.6667),
            new Pair<Number, Number>(21300.0000,13016.6667),
            new Pair<Number, Number>(21600.0000,14150.0000),
            new Pair<Number, Number>(21600.0000,14966.6667),
            new Pair<Number, Number>(21600.0000,16500.0000),
            new Pair<Number, Number>(22183.3333,13133.3333),
            new Pair<Number, Number>(22583.3333,14300.0000),
            new Pair<Number, Number>(22683.3333,12716.6667),
            new Pair<Number, Number>(23616.6667,15866.6667),
            new Pair<Number, Number>(23700.0000,15933.3333),
            new Pair<Number, Number>(23883.3333,14533.3333),
            new Pair<Number, Number>(24166.6667,13250.0000),
            new Pair<Number, Number>(25149.1667,12365.8333),
            new Pair<Number, Number>(26133.3333,14500.0000),
            new Pair<Number, Number>(26150.0000,10550.0000),
            new Pair<Number, Number>(26283.3333,12766.6667),
            new Pair<Number, Number>(26433.3333,13433.3333),
            new Pair<Number, Number>(26550.0000,13850.0000),
            new Pair<Number, Number>(26733.3333,11683.3333),
            new Pair<Number, Number>(27026.1111,13051.9444),
            new Pair<Number, Number>(27096.1111,13415.8333),
            new Pair<Number, Number>(27153.6111,13203.3333),
            new Pair<Number, Number>(27166.6667,9833.3333),
            new Pair<Number, Number>(27233.3333,10450.0000),
            new Pair<Number, Number>(27233.3333,11783.3333),
            new Pair<Number, Number>(27266.6667,10383.3333),
            new Pair<Number, Number>(27433.3333,12400.0000),
            new Pair<Number, Number>(27462.5000,12992.2222)
        ]
    }

    void testLoadPoints() {
        assert expected == TSPTestResources.sahara.points
    }
}