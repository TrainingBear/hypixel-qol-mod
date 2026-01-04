package com.trbear96

import com.trbear96.qol.core.Buttons
import com.trbear96.qol.core.TickScheduler
import com.trbear96.qol.core.currentTick
import com.trbear96.qol.perkembangan_teknologi.SawitGameplay
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient

class MySawit : ClientModInitializer {

    override fun onInitializeClient() {
        client = MinecraftClient.getInstance()
        Buttons

        SawitGameplay.tanamSawit()
        SawitGameplay.panenSawit()

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            val player = client.player
            if (player != null) {
                currentTick = player.age
            }
            TickScheduler.tick()
        }
    }
}