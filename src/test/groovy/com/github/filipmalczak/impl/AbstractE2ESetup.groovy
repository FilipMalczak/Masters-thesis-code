package com.github.filipmalczak.impl

import com.github.filipmalczak.ea.operators.GenderSelectionOperator
import com.github.filipmalczak.ea.operators.NaturalSelectionOperator
import com.github.filipmalczak.impl.choose_operator.RandomChoose
import com.github.filipmalczak.impl.choose_operator.TourneyChoose
import com.github.filipmalczak.impl.gender_selection.NoGender
import com.github.filipmalczak.impl.natural_selection.NaturalSelection
import com.github.filipmalczak.impl.tsp.Tour


trait AbstractE2ESetup {
    NaturalSelectionOperator<Tour> getNaturalSelection() {
        new NaturalSelection<Tour>(new TourneyChoose<Tour>(2))
    }

    GenderSelectionOperator<Tour> getGenderSelection() {
        new NoGender<Tour>(new RandomChoose<Tour>())
    }

    int getGendersCount() {
        1
    }
}
