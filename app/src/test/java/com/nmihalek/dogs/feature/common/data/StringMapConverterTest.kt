package com.nmihalek.dogs.feature.common.data

import org.junit.Assert.*
import org.junit.Test

class StringMapConverterTest {

    private val json = "{\"message\":[\"https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg\"]}"
    private val map = mapOf<String?, List<String>?>(
        "message" to listOf("https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg")
    )

    @Test
    fun `given string value, when convert called, should return map of list of strings`() {
        val expected = map
        val input = json

        val result = StringMapConverter.fromString(input)

        assertEquals(expected, result)
    }

    @Test
    fun `given map, when convert called, should return string form`() {
        val expected = json
        val input = map
        val result = StringMapConverter.fromStringMap(input)

        assertEquals(expected, result)
    }
}