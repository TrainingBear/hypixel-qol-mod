package com.trbear96.qol

import com.trbear96.client
import com.trbear96.qol.core.runTaskLater
import net.minecraft.client.option.KeyBinding

object GerakHandler {
    const val def = 10

    fun maju(tick: Int = def) = kontrol(client.options.forwardKey, tick)
    fun mundur(tick: Int = def) = kontrol(client.options.backKey, tick)
    fun kiri(tick: Int = def) = kontrol(client.options.leftKey, tick)
    fun kanan(tick: Int = def) = kontrol(client.options.rightKey, tick)
    fun lompat(tick: Int = def) = kontrol(client.options.jumpKey, tick)
    fun sprint(tick: Int = def) = kontrol(client.options.sprintKey, tick)
    fun shift(tick: Int = def) = kontrol(client.options.sneakKey, tick)
    fun klikKiri(tick: Int = def) = kontrol(client.options.leftKey, tick)
    fun klikKanan(tick: Int = def) = kontrol(client.options.rightKey, tick)

    fun kontrol(key: KeyBinding, delay: Int = 10){
        key.isPressed = true
        runTaskLater(delay){
            key.isPressed = false
        }
    }
}