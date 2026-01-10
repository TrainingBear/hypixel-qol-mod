package com.trbear96

import com.trbear96.qol.perkembangan_teknologi.RuteSawit
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import java.util.BitSet

lateinit var client : MinecraftClient
lateinit var bertani: KeyBinding
lateinit var menu: KeyBinding
val rute: RuteSawit
    get() = routes[picked]!!

val state = BitSet(256)

var picked: String = "Topaz"
val routes = mapOf(
    "Nirmala" to RuteSawit.Nirmala,
    "Dumpy" to RuteSawit.dumpy,
    "Topaz" to RuteSawit.topaz,
    "Yangambi" to RuteSawit.yangambi,
)