package io.github.victorhsr.hermes.core.ext

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StringExtTest {

    @Nested
    inner class CapitalizeTests {

        @Test
        fun `should capitalize lower case string`() {
            // given
            val input = "hello"
            val expectedResult = "Hello"

            // when
            val actual = input.myCapitalize()

            // then
            assertEquals(actual, expectedResult)
        }

        @Test
        fun `should capitalize upper case string`() {
            // given
            val input = "HELLO"
            val expectedResult = "HELLO"

            // when
            val actual = input.myCapitalize()

            // then
            assertEquals(actual, expectedResult)
        }

    }

    @Nested
    inner class UncapitalizeTests {

        @Test
        fun `should uncapitalize lower case string`() {
            // given
            val input = "hello"
            val expectedResult = "hello"

            // when
            val actual = input.uncapitalize()

            // then
            assertEquals(actual, expectedResult)
        }

        @Test
        fun `should uncapitalize upper case string`() {
            // given
            val input = "HELLO"
            val expectedResult = "hELLO"

            // when
            val actual = input.uncapitalize()

            // then
            assertEquals(actual, expectedResult)
        }

    }


}