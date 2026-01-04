package com.trbear96.qol.core

import net.minecraft.server.TickTask

object TickScheduler {

    private val tasks = mutableListOf<TickTask>()

    fun runLater(delay: Int, task: () -> Unit) {
        tasks += object : TickTask(delay) {
            override fun run() = task()
        }
    }

    fun runTimer(delay: Int, period: Int, task: () -> Unit) {
        tasks += object : TickTask(delay, period) {
            override fun run() = task()
        }
    }

    fun tick() {
        val it = tasks.iterator()
        while (it.hasNext()) {
            val task = it.next()

            if (task.delay-- > 0) continue

            task.run()

            if (task.period > 0) {
                task.delay = task.period
            } else {
                it.remove()
            }
        }
    }
}
