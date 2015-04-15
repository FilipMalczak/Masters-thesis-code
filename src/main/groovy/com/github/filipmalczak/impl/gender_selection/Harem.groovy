package com.github.filipmalczak.impl.gender_selection

import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.heuristics.Specimen
import com.github.filipmalczak.impl.choose_operator.ChooseOperator

import groovy.transform.Canonical

@Canonical
class Harem<S extends Specimen> extends AbstractGenderSelection<S>{
    int alphaCount
    double betaFactor
    ChooseOperator<S> chooseAlpha
    ChooseOperator<S> chooseBeta
    ChooseOperator<S> choosePartners
    int alphaGender = 0

    @Override
    List<List<S>> selectParentSets(List<S> population, int popSize, Context context) {
        int outSize = (int) Math.ceil(popSize*context.crossProb/1000)
        def out = []
        Map<Integer, List<S>> perGender = groupByGender(population)
        List<S> alphas = []
        alphaCount.times {
            S alpha = chooseAlpha.chooseOne(perGender[alphaGender], context, null)
            perGender[alphaGender].remove(alpha)
            alphas << alpha
        }
        int sneaksCount = (int) Math.ceil(outSize*betaFactor)
        int perAlpha = (int) Math.ceil((outSize - sneaksCount)/alphaCount)
        alphas.each { S alpha ->
            perAlpha.times {
                List<S> parents = [alpha]
                perGender.each { int gender, List<S> candidates ->
                    if (gender!=alphaGender)
                        parents << choosePartners.chooseOne(candidates, context, parents)
                }
                out << parents
            }
        }
        sneaksCount.times {
            List<S> parents = []
            parents << chooseBeta.chooseOne(population, context, null)
            perGender.each { int gender, List<S> candidates ->
                if (gender!=alphaGender)
                    parents << choosePartners.chooseOne(candidates, context, parents)
            }
            out << parents
        }
        out
    }

    @Override
    boolean usesGender() {
        true
    }

    @Override
    List<S> generateParentSet(List<S> population, Context context, Map<Integer, List<S>> grouped) {
        //unused
    }
}
