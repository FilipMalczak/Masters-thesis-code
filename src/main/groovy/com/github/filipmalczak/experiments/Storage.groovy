package com.github.filipmalczak.experiments

import org.nustaq.serialization.FSTConfiguration

import groovy.io.FileType
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

@Singleton
class Storage {
    boolean initialized = false
    File baseDir
    static final JsonSlurper slurper = new JsonSlurper()
    static final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration()

    static byte[] serialize(result) {
        conf.asByteArray(result)
    }

    static def deserialize(byte[] bytes) {
        conf.asObject(bytes)
    }

    static key(List<String> parts){
        parts.join("_")
    }

    void init(){
        if (!initialized){
            def storageConfig = new Properties()
            storageConfig.load(this.class.classLoader.getResourceAsStream("storage.properties"))
            baseDir = new File(storageConfig.getProperty("baseDir"))
            if (!baseDir.exists())
                baseDir.mkdirs()
            initialized = true
        }
    }

    File getResultsDir(){
        def out = new File(baseDir, "results")
        if (!out.exists())
            out.mkdirs()
        out
    }

    File getTreeFile(){
        def out = new File(baseDir, "tree.json")
        if (!out.exists())
            out.text = new JsonBuilder([:]).toPrettyString()
        out
    }

    static void recursiveMerge(Map map1, Map map2){
        map2.each { k, v ->
            if (map1[k] instanceof Map){
                assert v instanceof Map
                recursiveMerge(map1[k], v)
//            } else if (map1[k] instanceof Collection){
//                if (v instanceof Collection)
//                    map1[k].addAll(v)
//                else
//                    map1[k].add(v)
            } else
                map1[k] = v
        }
    }

    void updateTree(Map toMerge){
        Map upToDate = slurper.parse(treeFile)
        recursiveMerge(upToDate, toMerge)
        treeFile.text = new JsonBuilder(upToDate).toPrettyString()
    }

    File experimentDir(String name){
        def dir = new File(resultsDir, name)
    }

    File resultFile(String experimentName, String key){
        def dir = experimentDir(experimentName)
        if (!dir.exists())
            dir.mkdirs()
        new File(dir, key+".result")
    }

    def getResult(String experimentName, String key){
        def f = resultFile(experimentName, key)
        if (!f.exists())
            return null
        deserialize(f.bytes)
    }

    void putResult(String experimentName, String key, result){
        resultFile(experimentName, key).bytes = serialize(result)
    }

    /**
     *
     * @param c (String name, File dir)
     */
    void eachExperiment(Closure c){
        resultsDir.eachDir { File dir ->
            c.call(dir.name, dir)
        }
    }

    /**
     *
     * @param experimentName
     * @param c (List<String> paramValues, def result)
     */
    void eachResult(String experimentName, Closure c){
        experimentDir(experimentName).eachFile(FileType.FILES) { File f ->
            if (f.name.endsWith("result")) {
                def result = deserialize(f.bytes)
                c.call(f.name.replaceAll("[.]result", "").split("_").toList(), result)
            }

        }
    }
}
