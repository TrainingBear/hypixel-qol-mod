package com.trbear96

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.trbear96.qol.core.JsonConfig
import com.trbear96.qol.perkembangan_teknologi.RuteSawit
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import java.util.BitSet

lateinit var client: MinecraftClient
lateinit var bertani: KeyBinding
lateinit var menu: KeyBinding

/** seteruktur setelan
 * {
 *  "metodepanensawit": { // manen sawit
 *          "tipe":"blabla",
 *          "jagaenggrek": false
 *      }
 * }
 */
lateinit var config: ObjectNode
lateinit var confighandler: JsonConfig
var hasConfigChanged: Boolean = false

val rute: RuteSawit
    get() = routes[picked] ?: routes["Topaz"]!! //default

val state = BitSet(256)

lateinit var picked: String
var jagaenggrek: Boolean = false
var capek: Boolean = true
var kebalik: Boolean = false // COMING SOON
val routes = mapOf(
    "Nirmala" to RuteSawit.Nirmala,
    "Dumpy" to RuteSawit.dumpy,
    "Topaz" to RuteSawit.topaz,
    "Yangambi" to RuteSawit.yangambi,
)

fun aturUlangConfig(){
    picked = config.get(METODEPANENSAWIT).get(TIPE).asText()
    jagaenggrek = config.get(METODEPANENSAWIT).get(JAGAENGGREK).asBoolean()
    kebalik = config.get(METODEPANENSAWIT).get(KEBALIK).asBoolean()
    capek = config.get(METODEPANENSAWIT).get(CAPEK).asBoolean()
}

const val MYSAWIT = "mysawit"
const val METODEPANENSAWIT = "metodepanensawit"
const val TIPE = "tipe"
const val CAPEK = "kesel"
const val JAGAENGGREK = "jagaenggrek"
const val KEBALIK = "kebalik"