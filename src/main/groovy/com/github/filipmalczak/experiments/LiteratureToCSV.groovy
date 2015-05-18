package com.github.filipmalczak.experiments

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.Pair

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


class LiteratureToCSV implements Runnable{

//    Map<String, String> characters = LATEX
    Map<String, String> characters = CSV

    static List<String> literatureExperiments = [
        "tsp_sexual_ga",
        "tsp_gga",
        "knapsack_sexual_ga",
        "knapsack_gga"
    ]

    static Map<String, List<String>> headers = [
        sexual_ga: ["first", "second"],
        gga: ["chose_operator"]
    ]

    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("pl_PL")).with {
        it.decimalSeparator = ","
        it.groupingSeparator = "."
        it
    }
    DecimalFormat format = new DecimalFormat("#.0000", symbols)

    static final Map<String, String> CSV = [
        separator: ";",
        headerSeparator: "\n",
        newline: "\n",
        ext: ".csv"
    ]

    static final Map<String, String> LATEX = [
        separator: " & ",
        headerSeparator: " \\\\ \\hline \n",
        newline: " \\\\ \n",
        ext: ".tex"
    ]

    static Pair<Double, Double> avgStdDev(List l, Closure<Double> key = Closure.IDENTITY){
        def avg = l.sum(key)/l.size()
        def var = 0.0
        l.each {
            def diff = key(it) - avg
            var += diff*diff
        }
        var /= l.size()
        new Pair(avg, Math.sqrt(var))
    }

    String getNormal(List<Pair<List<String>, Context>> results){
        results.collect { Pair<List<String>, Context> result ->
            result.first + [
                format.format(result.second.globalBest.evaluate(result.second)),
                "${result.second.duration}"
            ].join(characters.separator)
        }.join(characters.newline)
    }

    String getStats(List<Pair<List<String>, Context>> results){
        Map<List<String>, List<Pair<List<String>, Context>>> grouped = results.groupBy {
            it.first[0..-2]
        }
        def out = "" << ""
        def order = grouped.keySet().toList()
        order.sort { it.toString() }
        order.each { List<String> params ->
            List<Pair<List<String>, Context>> res = grouped[params]
            def avgAndStd = avgStdDev(res, { it.second.globalBest.evaluate(it.second) })
            out << (params + [
                format.format(avgAndStd.first),
                format.format(avgAndStd.second)
            ]).join(characters.separator) << characters.newline

        }
        out.toString()
    }

    void doExperiment(String name){
        List<Pair<List<String>, Context>> results = Storage.instance.collectResults(name)
        if (results) {
            def normalOut = (
                headers[name.split("_").tail().join("_")] +
                    ["iteration", "eval", "duration"]
            ).join(characters.separator) << characters.headerSeparator
            def statsOut = (
                headers[name.split("_").tail().join("_")] +
                    ["mean", "stdDev"]
            ).join(characters.separator) << characters.headerSeparator
            normalOut << getNormal(results)
            statsOut << getStats(results)
            Storage.instance.summaryFile(name + characters.ext).text = normalOut.toString()
            Storage.instance.summaryFile(name + "_stats" + characters.ext).text = statsOut.toString()
        }
    }

    void run(Map<String, String> characters){
        this.characters = characters
        literatureExperiments.each {
            doExperiment(it)
        }
    }

    @Override
    void run() {
        [LATEX, CSV].each {
            run(it)
        }
    }

}
