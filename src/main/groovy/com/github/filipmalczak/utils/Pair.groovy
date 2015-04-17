package com.github.filipmalczak.utils

import groovy.transform.Canonical


@Canonical
class Pair<X, Y> implements Serializable{
    X first
    Y second
}
