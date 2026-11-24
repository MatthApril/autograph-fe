package com.example.autograph_fe.auth

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.File
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SecureTokenStorage(context: Context) {

    companion object {
        private const val KEY_ALIAS = "autograph_refresh_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
    }

    private val file = File(
        context.noBackupFilesDir,
        "refresh_token.enc"
    )

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
            .apply { load(null) }

        val existingKey = keyStore.getKey(
            KEY_ALIAS,
            null
        ) as? SecretKey

        if (existingKey != null) return existingKey

        val generator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        generator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or
                        KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(
                    KeyProperties.ENCRYPTION_PADDING_NONE
                )
                .setKeySize(256)
                .build()
        )

        return generator.generateKey()
    }

    @Synchronized
    fun saveRefreshToken(token: String) {
        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            getSecretKey()
        )

        val encrypted = cipher.doFinal(
            token.toByteArray(Charsets.UTF_8)
        )

        // Simpan IV (12 byte) diikuti ciphertext dan tag GCM.
        val output = cipher.iv + encrypted

        file.writeBytes(output)
    }

    @Synchronized
    fun getRefreshToken(): String? {
        if (!file.exists()) return null

        val bytes = file.readBytes()

        require(bytes.size > 28) {
            "Invalid encrypted token"
        }

        val iv = bytes.copyOfRange(0, 12)
        val encrypted = bytes.copyOfRange(12, bytes.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.DECRYPT_MODE,
            getSecretKey(),
            GCMParameterSpec(128, iv)
        )

        val decrypted = cipher.doFinal(encrypted)

        return String(decrypted, Charsets.UTF_8)
    }

    @Synchronized
    fun clearRefreshToken() {
        file.delete()
    }
}