package com.example.autograph_fe.ui.util

import java.util.Locale

/** 57 → "0:57", 125 → "2:05". */
fun formatCountdown(totalSeconds: Int): String {
    val seconds = totalSeconds.coerceAtLeast(0)
    return "%d:%02d".format(Locale.ROOT, seconds / 60, seconds % 60)
}
