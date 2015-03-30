package com.github.filipmalczak.ea.alg

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen


interface ContextHandler<S extends Specimen> {
    void update(List<S> population, Context context)
    void start(Context context)
    void finish(Context context)
}
