package com.github.filipmalczak.experiments

import com.github.filipmalczak.experiments.utils.Storage


class StorageTest extends GroovyTestCase {
    void testRecursiveMerge() {
        def x = [a: 1, b: [c: 3, d: [4, 5]]]
        def y = [b: [d: 6]]
        Storage.recursiveMerge(x, y)
        assert x == [a: 1, b: [c: 3, d: [6]]]
    }
}
