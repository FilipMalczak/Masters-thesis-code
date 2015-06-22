package com.github.filipmalczak.experiments.utils.formatting

import com.github.filipmalczak.experiments.utils.Storage
import com.github.filipmalczak.heuristics.Context

@Singleton
class Plotter {
    static final Map bounds = [
        :
//        initial_tsp: [0, 150_000],
//        initial_knapsack: [0, 250_000]
    ]

    static final ToGnuPlotTable tableProvider = new ToGnuPlotTable()

    String getScript(String expName, String key, String outPath, String dataPath, boolean title=true){
        return (title ? 'set title "'+key+'\\n'+expName+'"' : "" )+ """
set xlabel "Iteration#"
set ylabel "Eval"
set grid
set terminal pngcairo size 800,600 noenhanced font 'Verdana,10'
set output '${outPath}'
set bars 2.0
set style fill empty
plot '${dataPath}' using 1:3:2:6:5 with candlesticks title 'Variance' whiskerbars, \\
    ''         using 1:4:4:4:4 with candlesticks lt -1 notitle, \\
    ''         using 1:4 with linespoints lt 3 pt 13 title 'Mean'
    """
    }

    void plotIt(String expName, String key, boolean title=true){
        def ctx = Storage.instance.getResult(expName, key)
        Storage.instance.withTempDir { File dir ->
            try {
                tableProvider.renderTable(expName, key)
                def dataFile = Storage.instance.gnuplotDataFile(expName, key)
                def scriptFile = new File(dir, "script.gnuplot")
                scriptFile.text = getScript(expName, key,
                    Storage.instance.plotFile(expName, (title ? "" : "un") + "titled", key).absolutePath + ".png",
                    dataFile.absolutePath,
                    title
                )
                def pb = new ProcessBuilder("gnuplot ${scriptFile.absolutePath}".split(" "))
                pb.redirectErrorStream(true)
                pb.directory(dir)
                def p = pb.start()
                p.waitFor()
                try {
                    assert p.exitValue() == 0
                } catch (Throwable t) {
                    println p.inputStream.text
                    System.err.println p.errorStream.text
                    throw t
                }
            } catch (Throwable t){
                println "expName: $expName, key: $key"
                t.printStackTrace(System.out)
                //ignore; it means that there is no table under that key
            }
        }
    }

    void plotAll(boolean title=true){
        Storage.instance.eachExperiment { String name, File dir ->
            plotExperiment(name, title)
        }
    }

    void plotExperiment(String name, boolean title = true){
        Storage.instance.eachResult(name) { List<String> params, Context ctx ->
            try {
                plotIt(name, Storage.key(params), title)
            }catch (Throwable t){
                throw t
            }
        }
    }

    static void main(String... args){
        Storage.instance.init()
        Plotter.instance.plotAll(true)
//        Plotter.instance.plotIt("tweak_tsp", "60_0.0_0.58_0.1_110_roulette_2")
//        Plotter.instance.plotExperiment("tsp")
    }
}
