package com.trbear96

import com.trbear96.qol.perkembangan_teknologi.RuteSawit
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import java.util.BitSet

lateinit var client : MinecraftClient
lateinit var bertani: KeyBinding
lateinit var menu: KeyBinding
var rute: RuteSawit = RuteSawit.topaz
val state = BitSet(256)