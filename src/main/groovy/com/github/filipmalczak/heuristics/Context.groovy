package com.github.filipmalczak.heuristics

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Context<S extends Specimen> {
    // GA state
    int generation
    int mutProb
    int crossProb



    // fields for measuring time
    Date startTime = null
    Date endTime = null

    // history for analysis
    S globalBest = null

    List<Double> avgHistory = []
    List<Double> varianceHistory = []
    List<Double> bestHistory = []
    List<Double> worstHistory = []
    List<Double> CPHistory = []
    List<Double> MPHistory = []

    void pushAvg(double avg){
        avgHistory = [ avg ] + avgHistory
    }

    void pushVariance(double var){
        varianceHistory = [ var ] + varianceHistory
    }

    void pushBest(double best){
        bestHistory = [ best ] + bestHistory
    }

    void pushWorst(double worst){
        worstHistory = [ worst ] + worstHistory
    }

    void pushCP(double cp){
        CPHistory = [cp] + CPHistory
    }

    void pushMP(double mp) {
        MPHistory = [mp] + MPHistory
    }
}