package com.github.filipmalczak.utils



class RandomUtils {
    static Random r = new Random()

    static void init(long seed){
        r = new Random(seed)
    }

    static boolean happens(int probability) {
        Math.round(r.nextDouble()*1000) < probability
    }

    static <S> S random(List<S> population) {
        if (!population)
            return null
        population[r.nextInt(population.size())]
    }

    /**
     * [0, max)
     * @param max
     * @return
     */
    static int random(int max){
        r.nextInt(max)
    }

    /**
     * [from, to)
     * @param from
     * @param to
     * @return
     */
    static double random(double from, double to){
        r.nextDouble()*(to-from)+from
    }

    /**
     *
     * @param from
     * @param to
     * @return
     */
    static double normal(double from, double to){
        double width = to - from
        double mean = (to+from)/2
        // 2*sigma holds ~95% of observations
        double sigma = width/2
        mean + sigma*r.nextGaussian()
    }

    /**
     * [from, to)
     * @param from
     * @param to
     * @return
     */
    static int random(int from, int to){
        Math.min(to, Math.round(r.nextDouble())*(to-from)+from)
    }

    static List<Boolean> randomBools(int size, int prob){
        if (size <= 0)
            return []
        (1..size).collect { happens(prob) }
    }
}
