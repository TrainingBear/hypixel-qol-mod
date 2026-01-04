package com.trbear96.qol.perkembangan_teknologi

import com.trbear96.bertani
import com.trbear96.client
import com.trbear96.qol.core.onTick
import com.trbear96.qol.core.panen
import com.trbear96.rute
import net.minecraft.block.RedstoneBlock

object SawitGameplay {
    var pohon = false

    // initialization
    fun tanamSawit() {
        onTick {
            val player = client.player
            if (player != null) {
                val down = player.blockPos.down()
                val block = client.world!!.getBlockState(down).block
//                println("standing at block: "+block.name)
                pohon = block is RedstoneBlock
            }
        }
    }

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

            if(!rute.panen(player)) rute.berhenti()
            if(pohon) {
                "Edge has been reached"
                rute.tebangPohon(player)
                pohon = !pohon
            }
        }
    }
}