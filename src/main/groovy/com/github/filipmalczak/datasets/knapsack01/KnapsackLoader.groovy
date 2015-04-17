package com.github.filipmalczak.datasets.knapsack01

import com.github.filipmalczak.utils.Pair

class KnapsackLoader {
    KnapsackModel loadData(String resource, ClassLoader classLoader=null){
        if (classLoader==null)
            classLoader = this.class.classLoader
        loadData(classLoader.getResourceAsStream(resource))
    }

    KnapsackModel loadData(InputStream stream){
        def out = new KnapsackModel()
        out.data = []
        boolean data = false
        stream.eachLine { String line ->
            if (!line.startsWith("#")) {
                if (!data){
                    out.maxSize = Integer.parseInt(line)
                    data = true
                } else {
                    out.data << parseLine(line)
                }
            }
        }
        out
    }

    Pair<Integer, Integer> parseLine(String line){
        def tokens = line.split()
        assert tokens.size() == 2
        new Pair<Number, Number>(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]))
    }
}
