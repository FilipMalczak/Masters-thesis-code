package com.github.filipmalczak.ea.operators

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen

interface MutationOperator<S extends Specimen> {
    List<S> mutate(S s, Context context)
}