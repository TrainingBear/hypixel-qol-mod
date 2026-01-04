package com.trbear96.qol

import com.trbear96.client

object GerakHandler {
    fun liat(){
    }

    keyUp        // W
    keyDown      // S
    keyLeft      // A
    keyRight     // D
    keyJump      // SPACE
    keySprint    // CTRL
    keyShift     // SHIFT
    keyAttack    // LMB
    keyUse       // RMB
    fun maju(){
        client.options.keyUp.isDown = false
    }
}