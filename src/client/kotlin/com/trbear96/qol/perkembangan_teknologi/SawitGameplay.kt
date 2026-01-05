package com.trbear96.qol.perkembangan_teknologi

import com.trbear96.bertani
import com.trbear96.client
import com.trbear96.qol.core.getTargetBlock
import com.trbear96.qol.core.onTick
import com.trbear96.qol.core.panen
import com.trbear96.qol.core.runTaskLater
import com.trbear96.rute
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.RedstoneBlock
import kotlin.random.Random

object SawitGameplay {
    var pohon = false
    var final = false

    // initialization
    fun tanamSawit() {
        onTick {
            val player = client.player
            if (player != null) {
                val down = player.blockPos.down()
                val block = client.world!!.getBlockState(down).block
                pohon = block is RedstoneBlock
                final = block == Blocks.END_STONE
            }
        }
    }
    var flag = false

    fun panenSawit(){
        onTick {
            if(!panen) {
                it.close()
                println("Berhenti memanen..")
                rute.berhenti()
                return@onTick
            }
            val player = client.player
            if(player==null) return@onTick

            val berhasil = rute.panen(player)
            if(!berhasil) rute.berhenti()
            else {
                val rayResult = getTargetBlock()
                client.interactionManager?.attackBlock(rayResult.blockPos, player.facing)
                client.interactionManager?.updateBlockBreakingProgress(rayResult.blockPos, player.facing)
            }
            if(pohon && !flag) {
                println("Edge has been reached")
                pohon = !pohon
                flag = true
                rute.tebangPohon(player)
                runTaskLater(15){ flag = false }
            }
            if(final){
                final = false
                client.player?.networkHandler?.sendChatCommand("warp garden")
            }
            istirahat()
        }
    }

    fun istirahat(){
        if(0.00417f.rollChance()){
            println("Sedang istirahat... ")
            panen = false
            runTaskLater(Random.nextInt(30, 5)){
                rute.duid()
            }
        }
    }

    fun Float.rollChance(): Boolean {
        val roll = Random.nextInt(1, 1_000_001) // 0.0001 → 100.0000
        val chance = (this * 10_000).toInt()

        return roll <= chance
    }
}