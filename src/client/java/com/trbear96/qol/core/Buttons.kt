package com.trbear96.qol.core

import com.trbear96.bertani
import com.trbear96.client
import com.trbear96.menu
import com.trbear96.picked
import com.trbear96.qol.guiexample.MainHUD
import com.trbear96.rute
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW

var panen: Boolean = false
var openMenu: Boolean = false

object Buttons {
    val keyCategory = KeyBinding.Category.create(Identifier.ofVanilla("sawit"))

    init {
        bertani = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.indonesia.bertani",
                GLFW.GLFW_KEY_B,
                keyCategory
            )
        )

        menu = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.indonesia.menu",
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                keyCategory
            )
        )

        onTick {
            while (bertani.wasPressed()) {
                panen = !panen
                client.player?.sendMessage(
                    Text.literal("[MySawit] ")
                        .styled { it.withBold(true).withColor(Formatting.GREEN) }
                        .append(
                            Text.literal(if (panen) "Lanjut nandur $picked kang" else "Uwes kesel nandure kang?")
                                .styled { style -> style.withBold(false).withColor(Formatting.YELLOW) }
                        ),
                    false
                )
                rute.duid()
            }

            while (menu.wasPressed()) {
                openMenu = !openMenu
                if (openMenu) {
                    client.setScreen(MainHUD)
                    println("Membuka menu... ")
                }
            }
        }
    }
}
