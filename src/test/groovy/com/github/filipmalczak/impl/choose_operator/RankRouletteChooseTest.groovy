package com.github.filipmalczak.impl.choose_operator


class RankRouletteChooseTest extends GroovyTestCase {
    List<Integer> population = [*(1..10)] // wheel size: 55


    void testChoose() {
        assert RankRouletteChoose.choose(population, 8) == 1
        assert RankRouletteChoose.choose(population, 12) == 2
        assert RankRouletteChoose.choose(population, 30) == 4
        assert RankRouletteChoose.choose(population, 55) == 10
    }
}
