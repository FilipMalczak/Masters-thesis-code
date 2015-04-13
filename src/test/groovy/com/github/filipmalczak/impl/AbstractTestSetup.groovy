package com.github.filipmalczak.impl


trait AbstractTestSetup {
    int getGenerations() {
        30
    }

    double getCrossProb() {
        0.7
    }

    double getMutProb() {
        0.2
    }

    int getPopulationSize() {
        20
    }

    double getMixinFactor() {
        0.25
    }
}
