package com.github.filipmalczak.experiments

import static com.github.filipmalczak.experiments.DataSetConfig.*
println "RunAll"
Storage.instance.init()
if (executeStage.initial) {
    new InitialConfig().run()
    if (executeStage.csv)
        new ToCSV().run()
}
if (executeStage.tweak) {
    new TweakConfig().run()
    if (executeStage.csv)
        new ToCSV().run()
}
