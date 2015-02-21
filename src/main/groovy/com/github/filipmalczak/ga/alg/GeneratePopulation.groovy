package com.github.filipmalczak.ga.alg

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen


public interface GeneratePopulation<S extends Specimen> {
    List<S> generate(int size, Context context)
}