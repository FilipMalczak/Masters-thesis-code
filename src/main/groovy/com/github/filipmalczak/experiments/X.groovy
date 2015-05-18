package com.github.filipmalczak.experiments

import com.github.filipmalczak.datasets.ContextCategory
import com.github.filipmalczak.datasets.knapsack01.KnapsackResources
import com.github.filipmalczak.heuristics.Context

import groovy.transform.Canonical

import javax.management.DynamicMBean
import javax.management.MBeanServer
import javax.management.ObjectName
import javax.management.StandardMBean
import java.lang.management.ManagementFactory

def out = [:].withDefault {0}

[[0:23, 1:27]
 ,[0:27, 1:23]
 ,[0:22, 1:28]
 ,[1:29, 0:21]
 ,[1:26, 0:24]
 ,[:]
 ,[0:2, 1:3]
 ,[0:2, 1:3]
 ,[:]
 ,[0:2, 1:3]
 ,[:]
 ,[0:2, 1:3]
 ,[0:3, 1:2]
 ,[1:3, 0:2]
 ,[:]
 ,[0:5]
 ,[0:3, 1:2]
 ,[:]
 ,[1:3, 0:2]
 ,[1:1, 0:4]
 ,[:]
 ,[1:3, 0:2]
 ,[1:3, 0:2]
 ,[:]
 ,[1:3, 0:2]
 ,[0:4, 1:1]
 ,[:]
 ,[1:4, 0:1]
 ,[0:2, 1:3]
 ,[:]
 ,[1:3, 0:2]
 ,[0:2, 1:3]
 ,[:]
 ,[0:4, 1:1]
 ,[0:2, 1:3]
 ,[:]
 ,[0:3, 1:2]
 ,[0:1, 1:4]
 ,[:]
 ,[1:2, 0:3]
 ,[0:4, 1:1]
 ,[:]
 ,[1:1, 0:4]
 ,[1:1, 0:4]
 ,[:]
 ,[0:3, 1:2]
 ,[0:5]
 ,[:]
 ,[1:2, 0:3]
 ,[1:4, 0:1]
 ,[:]
 ,[1:2, 0:3]
 ,[0:2, 1:3]
 ,[:]
 ,[1:4, 0:1]
 ,[0:3, 1:2]
 ,[:]
 ,[1:2, 0:3]
 ,[0:3, 1:2]
 ,[:]
 ,[0:3, 1:2]
 ,[0:3, 1:2]
 ,[:]
 ,[1:3, 0:2]
 ,[1:4, 0:1]
 ,[:]
 ,[1:1, 0:4]
 ,[1:5]
 ,[:]
 ,[0:4, 1:1]
 ,[0:3, 1:2]
 ,[:]
 ,[0:4, 1:1]
 ,[1:3, 0:2]
 ,[:]
 ,[1:3, 0:2]
 ,[0:3, 1:2]
 ,[:]
 ,[0:2, 1:3]
 ,[0:5]
 ,[:]
 ,[0:3, 1:2]
 ,[1:3, 0:2]
 ,[:]
 ,[0:1, 1:4]
 ,[1:3, 0:2]
 ,[:]
 ,[1:4, 0:1]
 ,[0:4, 1:1]
 ,[:]
 ,[0:3, 1:2]
 ,[0:3, 1:2]
 ,[:]
 ,[1:5]
 ,[0:3, 1:2]
 ,[:]
 ,[1:2, 0:3]
 ,[1:4, 0:1]
 ,[:]
 ,[0:2, 1:3]
 ,[1:1, 0:4]
 ,[:]
 ,[0:1, 1:4]
 ,[1:3, 0:2]
 ,[:]
 ,[1:2, 0:3]
 ,[1:4, 0:1]
 ,[:]
 ,[0:2, 1:3]
 ,[1:3, 0:2]
 ,[:]
 ,[1:2, 0:3]
 ,[0:2, 1:3]
 ,[:]
 ,[0:3, 1:2]
 ,[0:2, 1:3]
 ,[:]
 ,[0:3, 1:2]
 ,[1:3, 0:2]
 ,[:]
 ,[1:4, 0:1]
 ,[0:4, 1:1]
 ,[:]
 ,[1:3, 0:2]
 ,[0:2, 1:3]
 ,[:]
 ,[0:3, 1:2]
 ,[1:4, 0:1]
 ,[:]
 ,[1:4, 0:1]
 ,[1:4, 0:1]
 ,[:]
 ,[0:2, 1:3]
 ,[0:3, 1:2]
 ,[:]
 ,[1:3, 0:2]
 ,[0:4, 1:1]
 ,[:]
 ,[0:1, 1:4]
 ,[1:3, 0:2]
 ,[:]
 ,[1:5]
 ,[1:3, 0:2]
 ,[:]
 ,[0:3, 1:2]
 ,[0:3, 1:2]
 ,[:]
 ,[1:2, 0:3]
 ,[1:3, 0:2]].each {
    it.each { k, v ->
        out[k]+=v
    }
}

println out

//interface AI {
//    int getX()
//    void setX(int x)
//    String report()
//    void stop()
//}
//
//@Canonical
//class A implements AI {
//    private int x = 0
//    boolean running = true
//
//    int getX() {
//        return x
//    }
//
//    void setX(int x) {
//        println "x from ${this.x} to $x"
//        this.x = x
//    }
//
//    @Override
//    String report() {
//        return "X = $x"
//    }
//
//    @Override
//    void stop() {
//        running = false
//    }
//}
//
//MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
////register the MBean
//def a = new A()
//ObjectName name = new ObjectName("com.github.filipmalczak:type=A");
//mbs.registerMBean(new StandardMBean(a, AI), name);
//while (a.running) ;
//
//
////println KnapsackResources.small
////println ContextCategory.toContext()
//
//
////
////import com.github.filipmalczak.heuristics.Context
////import com.github.filipmalczak.impl.rastrigin.Point
////
//////def ctx = new Context()
//////([
//////    [-5.12]*20,
////    [0]*5+[-5.12]*15,
////    [0]*10+[-5.12]*10,
////    [0]*20
////]+(2..10).collect{
////    [-5.12/it]*20
////}).each { List<Double> coords ->
////    def p = new Point(coords, 0)
////    println "-------------------------"
////    println "eval:   ${p.evaluate(ctx)}"
////    println "length: ${p.vectorLength}"
////    println "coords: ${p.coordinates}"
////}
////
////
