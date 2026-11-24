package com.example.autograph_fe

import com.example.autograph_fe.ui.components.sanitizeOtpInput
import org.junit.Assert.assertEquals
import org.junit.Test

class OtpInputTest {

    @Test
    fun pastedCodeWithSpace_keepsDigitsOnly() {
        assertEquals("482917", sanitizeOtpInput("482 917"))
    }

    @Test
    fun letters_areRemoved() {
        assertEquals("123", sanitizeOtpInput("1a2b3c"))
    }

    @Test
    fun extraDigits_areCutToSix() {
        assertEquals("123456", sanitizeOtpInput("12345678"))
    }

    @Test
    fun emptyInput_staysEmpty() {
        assertEquals("", sanitizeOtpInput(""))
    }
}
