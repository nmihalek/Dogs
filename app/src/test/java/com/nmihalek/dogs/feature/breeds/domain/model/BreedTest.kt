package com.nmihalek.dogs.feature.breeds.domain.model

import org.junit.Assert.*
import org.junit.Test

class BreedTest {

    @Test
    fun testFullName() {
        val breed = "spaniel"
        val subBreed = "cocker"
        val expected = "spaniel/cocker"
        val cSpan = Breed(breed, subBreed)

        assertEquals(expected, cSpan.fullBreed)
    }
}