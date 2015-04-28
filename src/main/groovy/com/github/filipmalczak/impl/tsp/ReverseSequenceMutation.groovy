package com.github.filipmalczak.impl.tsp

import com.github.filipmalczak.ea.operators.MutationOperator
import com.github.filipmalczak.heuristics.Context
import com.github.filipmalczak.utils.RandomUtils

import static com.github.filipmalczak.utils.RandomUtils.random

class ReverseSequenceMutation implements MutationOperator<Tour>{
    //src: http://arxiv.org/pdf/1203.3099.pdf // RSM

    static List<Integer> reverseSequence(List<Integer> sequence, int start, int stop){
        List<Integer> out = [ *sequence ]
//        out += start ? sequence[0..start-1] : []
        out[start..stop] = out[start..stop].reverse()
//        out += (stop+1)<(sequence.size()) ? [] : sequence[(stop+1)..(sequence.size()-1)]
//        try {
//            assert out.size() == out.toSet().size()
//        } catch (Throwable t){
//            throw t
//        }
        out
    }

    static List<Integer> reverseRandomSubsequence(List<Integer> sequence){
        def start = RandomUtils.random(sequence.size() - 2)
        def stop = RandomUtils.random(start+1, (sequence.size()-1))
        reverseSequence(sequence, start, stop)
    }

    @Override
    List<Tour> mutate(Tour tour, Context context) {
        def out = new Tour(
            reverseRandomSubsequence(tour.pointNumbers),
            tour.gender
        )
        [
            out
        ]
    }
}
