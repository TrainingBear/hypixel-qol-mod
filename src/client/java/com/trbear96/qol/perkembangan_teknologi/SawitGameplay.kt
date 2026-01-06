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
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import kotlin.random.Random

object SawitGameplay {
    var pohon = false
    var final = false
    @JvmField var breakingPos: BlockPos? = null;

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
        var swingCooldown = 0
        breakingPos = null
        onTick {
            if(!panen || client.world==null) {
                it.close()
                println("Berhenti memanen..")
                rute.berhenti()
                return@onTick
            }
            val player = client.player
            if(player==null) return@onTick

            val berhasil = rute.panen(player)
            if(!berhasil) {
                rute.berhenti()
                return@onTick
            }
            if(client.crosshairTarget !is BlockHitResult){
                breakingPos = null
                return@onTick
            }
            if (swingCooldown <= 0) {
                player.swingHand(Hand.MAIN_HAND);
                swingCooldown = 6; // ~300ms
            }

            if (swingCooldown > 0) {
                swingCooldown--;
            }
            val hit = client.crosshairTarget as? BlockHitResult
            if (hit == null || client.world!!.isAir(hit.blockPos)) {
                breakingPos = null
                return@onTick
            }

            if (breakingPos == null) {
                breakingPos = hit.blockPos
                client.interactionManager?.attackBlock(hit.blockPos, player.facing)
            } else if (breakingPos == hit.blockPos) {
                client.interactionManager?.updateBlockBreakingProgress(hit.blockPos, player.facing)
            }

            if(pohon && !flag) {
                println("Edge has been reached")
                pohon = !pohon
                flag = true
                rute.tebangPohon(player)
                runTaskLater(120){ flag = false }
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
            runTaskLater(Random.nextInt(30, 100)){
                println("Istirahat selesai")
                panen = true
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