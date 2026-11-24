package com.example.autograph_fe.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AutographMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Token FCM dibuat atau berubah.
        // Sinkronkan ke backend ketika sesi pengguna tersedia.
        Log.d("AutographFCM", "FCM token updated")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
        val body = message.notification?.body
        val noteId = message.data["noteId"]

        Log.d(
            "AutographFCM",
            "Message received: title=$title noteId=$noteId"
        )

        // Untuk menampilkan notifikasi ketika aplikasi foreground,
        // panggil NotificationHelper di sini.
    }
}