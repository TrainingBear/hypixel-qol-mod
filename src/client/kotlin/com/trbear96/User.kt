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

var picked: String = "Yangambi"
val routes = mapOf(
    "Yangambi" to RuteSawit.yangambi,
    "Dumpy" to RuteSawit.dumpy,
    "Topaz" to RuteSawit.topaz
)