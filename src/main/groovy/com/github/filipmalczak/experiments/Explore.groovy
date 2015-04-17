package com.github.filipmalczak.experiments

import groovy.util.logging.Slf4j
import groovyx.gpars.GParsPool

import static groovyx.gpars.GParsPool.withPool

import groovy.transform.Canonical

@Canonical
@Slf4j
class Explore {
    String name
    int repeats
    int reruns
    List<String> order
    Map<String, List> paramSets
    Map<String, Map<String, Object>> values = [:]

    Closure body // arg: Map<String, Object> realConfig; return: result
    Closure onParamFound // args: String paramName, def val, def realVal
    Closure chooseBest // arg: Map<Object, List> results; return: param val == key

    int poolSize = 9

    Map<String, Object> realize(Map<String, Object> config){
        def out = [:]
        config.each { k, v ->
            out[k] = values.containsKey(k) ? values[k][v] : v
        }
        out
    }


    String key(Map<String, Object> config){
        Storage.key((order+["iteration"]).collect {
            config[it] instanceof List ?
                Storage.key(config[it].collect { it2 -> "$it2" }) :
                "${config[it]}"}
        )
    }

    void run(){
        log.info "Exploration experiment: $name"
        Map<String, Object> current = [:]
        paramSets.each { k, v ->
            current[k] =  v[0]
        }
        def gpool = new GParsPool()
        def pool = gpool.createPool(poolSize)
        reruns.times {
            log.info("Run $it")
            order.each { String param ->
                Map<Object, List> paramResults = [:].withDefault { [].asSynchronized() }.asSynchronized()
                gpool.withExistingPool(pool) {
                    paramSets[param].eachParallel { val ->
                        gpool.withExistingPool(pool) {
                            (0..repeats - 1).eachParallel { int i ->
                                Map localConfig = current + [iteration: i, (param): val]
                                String k = key(localConfig)
                                log.info("${Thread.currentThread().name}: Checking key $k")
                                def result = Storage.instance.getResult(name, k)
                                if (result == null) {
                                    log.info "${Thread.currentThread().name}: No saved result, calling body"
                                    result = body.call(realize(localConfig))
                                    Storage.instance.putResult(name, k, result)
                                }
                                paramResults[val] << result
                            }
                        }
                    }
                }
                def bestVal = chooseBest(paramResults)
                onParamFound.call(param, bestVal, values.containsKey(param) ? values[param][bestVal]: bestVal)
                current[param] = bestVal
                log.info "Using $param = $bestVal"
            }
            log.info "Base config: ${order.collect { "$it = ${current[it]}" }.join(";")}"
        }
    }
}
