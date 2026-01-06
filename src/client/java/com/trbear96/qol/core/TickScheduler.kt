package com.trbear96.qol.core

import java.io.Closeable

var currentTick: Int = 0

fun onTick(task: (Closeable) -> Unit){
    TickScheduler.scheduleTimer(1, 1, task)
}

fun runTaskLater(delay: Int, task: (Closeable) -> Unit){
    TickScheduler.scheduleLater(delay, task)
}

object TickScheduler {
    private val laterQueue = mutableListOf<TickTask>()
    private val tickQueue = mutableListOf<TickTask>()
    private val laterScheduler = mutableListOf<TickTask>()
    private val tickScheduler = mutableListOf<TickTask>()

    fun scheduleLater(delay: Int, task: (Closeable) -> Unit) {
        laterQueue += TickTask(delay, 0, task)
    }

    fun scheduleTimer(delay: Int, period: Int, task: (Closeable) -> Unit) {
        tickQueue += TickTask(delay, period, task)
    }

    fun tick() {
        laterScheduler+=laterQueue
        laterQueue.clear()
        tickScheduler+=tickQueue
        tickQueue.clear()
        val laterIterator = laterScheduler.iterator()
        while (laterIterator.hasNext()) {
            val task = laterIterator.next()
            if(task.cancelled){
                laterIterator.remove()
                continue
            }
            task.delay--
            if (task.delay >= 0) continue
            task.tick()
            laterIterator.remove()
        }

        val tickIterator = tickScheduler.iterator()
        while (tickIterator.hasNext()) {
            val task = tickIterator.next()
            if(task.cancelled){
                tickIterator.remove()
                continue
            }
            if (--task.delay >= 0) continue

            task.tick()
            if(task.cancelled){
                tickIterator.remove()
                continue
            }
            task.delay = task.period - 1
        }
    }
}
