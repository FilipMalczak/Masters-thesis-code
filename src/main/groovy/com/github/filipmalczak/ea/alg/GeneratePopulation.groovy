package com.github.filipmalczak.ea.alg

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen


public interface GeneratePopulation<S extends Specimen> {
    List<S> generate(int size, Context<S> context)
    int getGenderCount()
}