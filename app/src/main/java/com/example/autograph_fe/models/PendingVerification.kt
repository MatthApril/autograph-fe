package com.example.autograph_fe.models

/** What the server says about a code it just sent. */
data class OtpTiming(
    val email: String,
    val expiresInSeconds: Int,
    val resendInSeconds: Int,
)

/** Everything the Verify email screen needs to start. */
data class PendingVerification(
    val email: String,
    val expiresInSeconds: Int,
    val resendInSeconds: Int,
    val needsNewCode: Boolean,   // true when the user registered again with a pending email
)
