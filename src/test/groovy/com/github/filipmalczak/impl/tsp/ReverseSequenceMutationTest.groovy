package com.github.filipmalczak.impl.tsp

class ReverseSequenceMutationTest extends GroovyTestCase {
    void testReverseSubsentence() {
        assert ReverseSequenceMutation.reverseSequence([0, 1, 2, 3, 4, 5], 2, 4) == [0, 1, 4, 3, 2, 5]
        assert ReverseSequenceMutation.reverseSequence([0, 1, 2, 3, 4, 5], 0, 3) == [3, 2, 1, 0, 4, 5]
        assert ReverseSequenceMutation.reverseSequence([0, 1, 2, 3, 4, 5], 2, 5) == [0, 1, 5, 4, 3, 2]
    }
}
