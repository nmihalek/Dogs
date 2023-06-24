package com.nmihalek.dogs.feature.common.presentation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TitleViewModelTest {

    lateinit var sut: TitleViewModel

    @Before
    fun setUp() {
        sut = TitleViewModel()
    }

    @Test
    fun `when title set, should change title`() {
        val newTitle = "new"
        assertEquals(sut.title, "")

        sut.setAppTitle(newTitle)

        assertEquals(newTitle, sut.title)
    }
}