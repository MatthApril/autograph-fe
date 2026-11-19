package com.example.autograph_fe

import com.example.autograph_fe.ui.screens.auth.AuthValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun emptyEmail_returnsRequiredError() {
        assertEquals(R.string.error_email_required, AuthValidator.email(""))
    }

    @Test
    fun emailWithoutDomain_returnsInvalidError() {
        assertEquals(R.string.error_email_invalid, AuthValidator.email("maya@"))
    }

    @Test
    fun validEmail_returnsNoError() {
        assertNull(AuthValidator.email("maya@example.com"))
    }

    @Test
    fun sevenCharacterPassword_isTooShort() {
        assertEquals(R.string.error_password_short, AuthValidator.newPassword("1234567"))
    }

    @Test
    fun differentPasswords_returnMismatchError() {
        assertEquals(R.string.error_password_mismatch, AuthValidator.confirmPassword("abcdefgh", "abcdefgx"))
    }
}
