package com.github.filipmalczak.experiments

import org.nustaq.serialization.FSTConfiguration

import groovy.io.FileType
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import java.util.concurrent.atomic.AtomicInteger

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

    static File ensuredDir(File dir){
        if (!dir.exists())
            dir.mkdirs()
        dir
    }

    static File ensuredFile(Map args=[:], File file){
        if (!file.exists()) {
            ensuredDir(file.parentFile)
            args.each { k, v -> file."$k"=v }
        }
        file
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

    void init(){
        if (!initialized){
            def storageConfig = new Properties()
            storageConfig.load(this.class.classLoader.getResourceAsStream("storage.properties"))
            baseDir = new File(storageConfig.getProperty("baseDir"))
            if (!baseDir.exists())
                baseDir.mkdirs()
            initialized = true
            tempBaseDir.deleteOnExit()
        }
    }

    @Lazy File resultsDir = ensuredDir(new File(baseDir, "results"))

    @Lazy File treeFile = ensuredFile(new File(baseDir, "tree.json"), text: new JsonBuilder([:]).toPrettyString())

    @Lazy File summariesDir = ensuredDir(new File(baseDir, "summaries"))

    @Lazy File plotsDir = ensuredDir(new File(baseDir, "plots"))

    @Lazy File tempBaseDir = ensuredDir(File.createTempDir())

    File experimentDir(String name){
        ensuredDir(new File(resultsDir, name))
    }

    File resultFile(String experimentName, String key){
        new File(experimentDir(experimentName), key+".result")
    }

    File summaryFile(String name){
        ensuredFile(new File(summariesDir, name), text: "")
    }

    File plotSubdir(String experimentName, String subdir){
        ensuredDir(new File(new File(plotsDir, experimentName), subdir))
    }

    File plotFile(String experimentName, String subdir, String key){
        new File(plotSubdir(experimentName, subdir), key)
    }

    protected final AtomicInteger dirCounter = new AtomicInteger(-1)
    protected final AtomicInteger fileCounter = new AtomicInteger(-1)
    protected final List<File> availableTempFiles = [].asSynchronized()
    protected final List<File> usedTempFiles = [].asSynchronized()



    public <T> T withTempDir(Closure<T> closure){
        def number = dirCounter.addAndGet(1)
        def dir = ensuredDir(new File(tempBaseDir, "$number"))
        try {
            return closure.call(dir)
        } finally {
            dir.delete()
        }
    }

    public <T> T withTempFile(Closure<T> closure){
        def file
        try {
            file = availableTempFiles.pop()
        } catch (NoSuchElementException nsee) {
            def number = fileCounter.addAndGet(1)
            file = ensuredFile(tempBaseDir, "$number", bytes: [])
            usedTempFiles.add(file)
        }
        try {
            return closure.call(file)
        } finally {
            file.bytes = []
            usedTempFiles.remove(file)
            availableTempFiles.add(file)
        }
    }

    void updateTree(Map toMerge){
        Map upToDate = treeSnapshot
        recursiveMerge(upToDate, toMerge)
        treeFile.text = new JsonBuilder(upToDate).toPrettyString()
    }

    Map getTreeSnapshot(){
        slurper.parse(treeFile)
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
                try {
                    def result = deserialize(f.bytes)
                    c.call(f.name.replaceAll("[.]result", "").split("_").toList(), result)
                } catch (Throwable t){
                    throw t
                }
            }

        }
    }


}
