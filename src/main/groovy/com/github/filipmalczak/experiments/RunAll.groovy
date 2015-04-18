package com.github.filipmalczak.experiments

import com.github.filipmalczak.ea.operators.GenderSelectionOperator
import com.github.filipmalczak.ea.operators.NaturalSelectionOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.gender_selection.NoGender
import com.github.filipmalczak.impl.tsp.TSPSetup
import com.github.filipmalczak.impl.tsp.Tour

Storage.instance.init()
new InitialConfig().run()
new ToCSV().run()
new TweakConfig().run()
new ToCSV().run()