package com.github.filipmalczak.experiments.utils.formatting

import com.github.filipmalczak.experiments.utils.Storage
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.Pair

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ResearchToTable implements Runnable{
    String name = "tsp"
    Map<String, String> characters = LATEX

    ResearchToTable(String name, Map<String, String> characters) {
        this.name = name
        this.characters = characters
    }

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



    String doGenSel(String genSel, Map<List<String>, List<Pair<List<String>, Context>>> resultsMap){
        def out = "" << ""
        def order = resultsMap.keySet().toList()
        order.sort { it.toString() }
        order.each { List<String> params ->
            List<Pair<List<String>, Context>> results = resultsMap[params]
            results.sort { it.first.last() }
            results.each { Pair<List<String>, Context> result ->
                final def ctx = result.second
                final def i = result.first.last()
                out << [
                    genSel,
                    params.toString().replaceAll("\\[", "(").replaceAll("\\]", ")"),
                    i,
                    format.format(ctx.globalBest.evaluate(ctx)),
                    "${ctx.duration}"
                ].join(characters.separator) << characters.newline
            }
        }
        out.toString()
    }

    @Override
    void run() {
        def grouped = Storage.instance.collectResults(name).groupBy(
            { Pair<List<String>, Context> p ->
                p.first.head()
            },
            { Pair<List<String>, Context> p ->
                p.first.subList(1, p.first.size()-1)
            }
        )
        def out = ([
            "genderSelection",
            "params",
            "iteration",
            "eval",
            "duration"
        ].join(characters.separator)) << characters.headerSeparator
        def order = grouped.keySet().toList().sort()
        order.each {
            out << doGenSel(it, grouped[it])
        }
        Storage.instance.summaryFile("$name${characters.ext}").text = out.toString()
    }
}
