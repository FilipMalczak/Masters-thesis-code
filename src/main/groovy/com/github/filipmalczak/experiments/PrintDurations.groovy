package com.github.filipmalczak.experiments

import com.github.filipmalczak.heuristics.Context

import groovy.time.TimeCategory
import groovy.time.TimeDuration

def normalize(TimeDuration d){
    int months = d.months
    int days = d.days
    int hours = d.hours
    int minutes = d.minutes
    int seconds = d.seconds
    minutes += Math.floorDiv(seconds, 60)
    seconds = seconds % 60
    hours += Math.floorDiv(minutes, 60)
    minutes = minutes % 60
    days += Math.floorDiv(hours, 24)
    hours = hours % 24
    months += Math.floorDiv(days, 30)
    days = days % 30
    def out
    use(TimeCategory){
        out = months.months + days.days + hours.hours + minutes.minutes + seconds.seconds
    }
}

def divide(TimeDuration d, int x){
    normalize(
        TimeCategory.getSeconds(
            Math.floorDiv(
                d.seconds + d.minutes*60 + d.hours*60*60 + d.days*24*60*60 + d.months*30*24*60*60,
                x
            )
        )
    )
}

Map durations = [:].withDefault { new TimeDuration(0,0,0,0) }

List toPrint = []

Storage.instance.eachExperiment { String e, File dir ->
    Storage.instance.eachResult(e){ l, Context r ->
        durations[e] += r.duration
    }
    toPrint << "${e.padRight(25)}| ${normalize(durations[e]).toString().padRight(55)}| ${divide(durations[e], 8).toString().padRight(55)}"
}
def header = "${"experiment".padRight(25)}| ${"total".padRight(55)}| per core"
def footer = "${'SUM'.padRight(25)}| ${normalize(durations.values().sum()).toString().padRight(55)}| ${divide(durations.values().sum(), 8).toString().padRight(55)}"

int maxLength = ([header, footer] + toPrint).collect {it.size()}.max()

println header
println "-"*maxLength
toPrint.sort().each this.&println
println "="*maxLength
println footer