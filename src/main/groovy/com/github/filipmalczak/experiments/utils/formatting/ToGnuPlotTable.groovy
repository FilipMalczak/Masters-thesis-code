package com.github.filipmalczak.experiments.utils.formatting

import com.github.filipmalczak.experiments.utils.Storage
import com.github.filipmalczak.heuristics.Context


class ToGnuPlotTable {
    List<List<Number>> generateTable(Context context){
        def out = []
        def avgs = context.avgHistory.reverse()
        if (!avgs)
            return null
        def worsts = context.worstHistory.reverse()
        def bests = context.bestHistory.reverse()
        def vars = context.varianceHistory.reverse()
        avgs.eachWithIndex { double avg, int i ->
//            def stdDev = Math.sqrt(vars[i])
            def stdDev = vars[i]
            def toAdd = [
                i,
                worsts[i],
                avg-stdDev,
                avg,
                avg+stdDev,
                bests[i]
            ]
            out.add toAdd
        }
        out
    }

    String generateDataFile(Context context){
        generateTable(context)?.collect {
            it.collect { x -> "$x"}.join("\t")
        }?.join("\n")
    }

    String getTable(String expName, String key){
        def ctx = Storage.instance.getResult(expName, key)
        ctx ? generateDataFile(ctx) : null
    }

    void renderTable(String expName, String key){
        def table = getTable(expName, key)
        assert table
        Storage.instance.gnuplotDataFile(expName, key).text = table
    }

    static void main(String[] args){
        def exp = "tsp_tweak"//args[0]
        def key = "50_0.0_0.58_0.1_105_roulette_2"//args[1]
        new ToGnuPlotTable().renderTable(exp, key)
    }
}
