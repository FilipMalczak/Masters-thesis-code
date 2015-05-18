package com.github.filipmalczak.heuristics

import groovy.time.TimeCategory
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class Context<S extends Specimen> implements Serializable{
    Map<String, Object> problemDefinition = [:]

    // GA state
    int generation = 0
    int mutProb = 0
    int crossProb = 0

    // fields for measuring time
    Date startTime = null
    Date endTime = null

    // history for analysis
    S globalBest = null


    //history, optional
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

    def getDuration(){
        if (!(startTime && endTime))
            return null
        use(TimeCategory){
            endTime - startTime
        }
    }
}