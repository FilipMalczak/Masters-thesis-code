package com.github.filipmalczak.datasets.tsp

import can.i.has.utils.Pair


class TSPLoader {
    final static String DATA_START = "NODE_COORD_SECTION"
    final static String EOF = "EOF"


    List<Pair<Number, Number>> loadPoints(String resource, ClassLoader classLoader=null){
        if (classLoader==null)
            classLoader = this.class.classLoader
        loadPoints(classLoader.getResourceAsStream(resource))
    }

    List<Pair<Number, Number>> loadPoints(InputStream stream){
        def out = []
        boolean data = false
        boolean nextLine = false
        stream.eachLine { String line ->
            if (line == DATA_START)
                nextLine = true
            if (line == EOF)
                data = false

            if (data)
                out << parsePoint(line)
            data = nextLine
        }
        out
    }

    Pair<Number, Number> parsePoint(String line){
        def tokens = line.split()
        assert tokens.size() == 3
        new Pair<Number, Number>(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]))
    }
}
