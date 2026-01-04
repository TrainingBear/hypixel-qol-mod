package com.trbear96

import com.trbear96.qol.core.TickScheduler
import com.trbear96.qol.core.currentTick
import com.trbear96.qol.perkembangan_teknologi.SawitGameplay
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

class MySawit : ClientModInitializer {
    val keyCategory = KeyBinding.Category.create(Identifier.ofVanilla("key.category.trbear96"))

    override fun onInitializeClient() {
        client = MinecraftClient.getInstance()

        bertani = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.indonesia.bertani",
                GLFW.GLFW_KEY_B,
                keyCategory
            )
        )

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