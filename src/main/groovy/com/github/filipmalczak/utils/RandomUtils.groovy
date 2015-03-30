package com.github.filipmalczak.utils



class RandomUtils {
    static Random r = new Random()

    static boolean happens(int probability) {
        Math.round(r.nextDouble()*1000) < probability
    }

    static <S> S random(List<S> population) {
        if (!population)
            return null
        population[r.nextInt(population.size())]
    }

    static int random(int max){
        r.nextInt(max)
    }

    static double random(double from, double to){
        r.nextDouble()*(to-from)+from
    }

    static List<Boolean> randomBools(int size, int prob){
        if (size <= 0)
            return []
        (1..size).collect { happens(prob) }
    }
}
