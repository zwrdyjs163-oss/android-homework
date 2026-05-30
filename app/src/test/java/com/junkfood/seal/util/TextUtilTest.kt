package com.junkfood.seal.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TextUtilTest {

    @Test
    fun toDurationText_formatsMinutesAndHours() {
        assertEquals("01:05", 65.toDurationText())
        assertEquals("1:01:01", 3661.toDurationText())
    }

    @Test
    fun isNumberInRange_rejectsBlankNonDigitsAndOutOfRangeValues() {
        assertTrue("42".isNumberInRange(1, 100))
        assertFalse("".isNumberInRange(1, 100))
        assertFalse("4.2".isNumberInRange(1, 100))
        assertFalse("101".isNumberInRange(1, 100))
    }

    @Test
    fun findURLsFromString_returnsAllMatchesOrFirstMatchOnly() {
        val text = "Watch https://example.com/video?id=1 and http://seal.example.org/path"

        assertEquals(
            listOf("https://example.com/video?id=1", "http://seal.example.org/path"),
            findURLsFromString(text),
        )
        assertEquals(listOf("https://example.com/video?id=1"), findURLsFromString(text, true))
    }

    @Test
    fun toHttpsUrl_upgradesHttpAndHandlesNull() {
        assertEquals("https://example.com/video", "http://example.com/video".toHttpsUrl())
        val missingUrl: String? = null

        assertEquals("https://example.com/video", "https://example.com/video".toHttpsUrl())
        assertEquals("", missingUrl.toHttpsUrl())
    }

    @Test
    fun connectWithDelimiter_ignoresBlankParts() {
        assertEquals("video + audio", connectWithDelimiter("video", "", null, "audio", delimiter = " + "))
    }

    @Test
    fun toBitrateText_formatsKbpsAndMbps() {
        assertEquals("512.0 Kbps", 512.toBitrateText())
        assertEquals("2.00 Mbps", 2048.toBitrateText())
        assertEquals("", 0.toBitrateText())
    }
}
