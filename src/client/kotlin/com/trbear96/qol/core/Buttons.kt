package com.trbear96.qol.core

import com.trbear96.bertani
import com.trbear96.client
import com.trbear96.menu
import com.trbear96.qol.GUI
import com.trbear96.qol.createMainWindow
import com.trbear96.qol.perkembangan_teknologi.SawitGameplay
import com.trbear96.rute
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
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
                rute.duid()
            }
        }

        while (menu.wasPressed()) {
            openMenu = !openMenu
            if (openMenu) {
                client.setScreen(GUI(createMainWindow()))
            }
        }
    }
}
