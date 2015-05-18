package com.github.filipmalczak.experiments

import com.github.filipmalczak.heuristics.Context

@Singleton
class Plotter {
    static final Map bounds = [
        :
//        initial_tsp: [0, 150_000],
//        initial_knapsack: [0, 250_000]
    ]

    String generateDataFile(Context context, def width=0.3){
        def out = "" << ""
        def avgs = context.avgHistory.reverse()
        if (!avgs)
            return null
        def worsts = context.worstHistory.reverse()
        def bests = context.bestHistory.reverse()
        def vars = context.varianceHistory.reverse()
        avgs.eachWithIndex { double avg, int i ->
//            def stdDev = Math.sqrt(vars[i])
            def stdDev = vars[i]
            out << [
                i,
                worsts[i],
                avg-stdDev,
                avg,
                avg+stdDev,
                bests[i],
                width,
                i
            ].collect {"${it}"}.join("\t")
            out << "\n"
        }
        return out.toString()
    }


    String getScript(String expName, String key, String outPath, String dataPath, boolean title=true){
        return (title ? 'set title "'+key+'\\n'+expName+'"' : "" )+ """
set xlabel "Iteration#"
set ylabel "Eval"
set grid
set terminal pngcairo size 800,600 noenhanced font 'Verdana,10'
set output '${outPath}'
set bars 2.0 """+
            (bounds[expName] ? """set xrange [*<${bounds[expName][0]}:${bounds[expName][1]}<*]
""" : "\n")+ """set style fill empty
plot '${dataPath}' using 1:3:2:6:5:xticlabels(8) with candlesticks title 'Variance' whiskerbars, \\
    ''         using 1:4:4:4:4 with candlesticks lt -1 notitle, \\
    ''         using 1:4 with linespoints lt 3 pt 13 title 'Mean'
    """
    }

    void plotIt(String expName, String key, boolean title=true){
        def ctx = Storage.instance.getResult(expName, key)
        Storage.instance.withTempDir { File dir ->
            def dataFile = new File(dir, "data.dat")
            def data = generateDataFile(ctx)
            if (data) {
                dataFile.text = data
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
//        Plotter.instance.plotAll(true)
//        Plotter.instance.plotIt("initial_tsp", "100_0.0_0.8_0.1_100_roulette_0")
        Plotter.instance.plotExperiment("tsp")
    }
}
