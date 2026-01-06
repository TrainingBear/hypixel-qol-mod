package com.trbear96.qol.core

import java.io.Closeable

class TickTask(
    var delay: Int,
    val period: Int,
    private val handle: (Closeable) -> Unit
) {
    var cancelled = false
        private set

    private val closeable = Closeable { cancelled = true }

    fun tick() {
        handle(closeable)
    }
}
