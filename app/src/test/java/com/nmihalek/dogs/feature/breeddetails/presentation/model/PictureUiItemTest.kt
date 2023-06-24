package com.nmihalek.dogs.feature.breeddetails.presentation.model

import org.junit.Assert.*
import org.junit.Test

class PictureUiItemTest {
    @Test
    fun `test fullName`() {
        val sut = PictureUiItem(breed = "spaniel", subBreed = "cocker", imageUrl = "", isFavourite = false)
        val expected = "spaniel/cocker"

        assertEquals(expected, sut.fullBreed)
    }
}