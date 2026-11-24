package com.example.autograph_fe

import com.example.autograph_fe.ui.util.formatCountdown
import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormatTest {

    @Test
    fun underOneMinute() = assertEquals("0:57", formatCountdown(57))

    @Test
    fun singleDigitSeconds_arePadded() = assertEquals("0:05", formatCountdown(5))

    @Test
    fun overOneMinute() = assertEquals("2:05", formatCountdown(125))

    @Test
    fun negative_showsZero() = assertEquals("0:00", formatCountdown(-3))
}
