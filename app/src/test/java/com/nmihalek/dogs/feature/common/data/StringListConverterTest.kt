package com.nmihalek.dogs.feature.common.data

import org.junit.Assert.*
import org.junit.Test

class StringListConverterTest {

    @Test
    fun `given string list passed, when converting, should return concated strings`() {
        val input = listOf("first", "second")
        val result = StringListConverter.toString(input)

        assertEquals("first,second", result)
    }

    @Test
    fun `given concated string passed, when converting, should return list of strings`() {
        val expected = listOf("first", "second")
        val input = "first,second"
        val result = StringListConverter.fromString(input)

        assertEquals(expected, result)
    }
}
