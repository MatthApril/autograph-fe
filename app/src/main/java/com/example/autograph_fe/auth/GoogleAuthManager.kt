package com.example.autograph_fe.ui.auth

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthManager(
    private val activity: Activity
) {
    private val credentialManager = CredentialManager.create(activity)

    suspend fun getGoogleCreds(webClientId: String): String {
        Log.d("GoogleAuth", "Package: ${activity.packageName}")
        Log.d("GoogleAuth", "Web Client ID: $webClientId")

        val googleOption = GetSignInWithGoogleOption.Builder(
            serverClientId = webClientId
        ).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                context = activity,
                request = request
            )

            val credential = result.credential

            if (
                credential is CustomCredential &&
                credential.type ==
                GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)

                Log.d("GoogleAuth", "ID Token received successfully")

                return googleCredential.idToken
            }

            throw IllegalStateException("Invalid Google credential")

        } catch (e: GetCredentialException) {
            Log.e("GoogleAuth", "Credential type: ${e.type}")
            Log.e("GoogleAuth", "Credential error: ${e.message}", e)
            throw e
        }
    }
}