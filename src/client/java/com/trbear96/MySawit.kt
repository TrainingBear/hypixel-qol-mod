package com.trbear96

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.trbear96.qol.core.Buttons
import com.trbear96.qol.core.JsonConfig
import com.trbear96.qol.core.JsonConfig.Companion.getOrMakeObject
import com.trbear96.qol.core.JsonConfig.Companion.jsonHandler
import com.trbear96.qol.core.TickScheduler
import com.trbear96.qol.core.currentTick
import com.trbear96.qol.perkembangan_teknologi.RuteSawit
import com.trbear96.qol.perkembangan_teknologi.SawitGameplay
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient

class MySawit : ClientModInitializer {

    override fun onInitializeClient() {
        client = MinecraftClient.getInstance()
        Buttons

        confighandler = JsonConfig(null, "mysawit", true, autoLoad = true)
        val node: ObjectNode = confighandler.createOrGet(MYSAWIT) ?: throw NullPointerException("mysawit cannot be created")
        if (node.isEmpty) { // pengalaman pertama
            node.getOrMakeObject(METODEPANENSAWIT).apply {
                put(TIPE, routes.keys.firstOrNull() ?: "Topaz") //jaga2
                put(JAGAENGGREK, false)
                put(KEBALIK, false)
                put(CAPEK, true)
            } // tipe DEFAULT
            confighandler.save(MYSAWIT)
        }
        config = node
        aturUlangConfig()
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