package com.trbear96.qol.perkembangan_teknologi

import com.trbear96.bertani
import com.trbear96.capek
import com.trbear96.client
import com.trbear96.jagaenggrek
import com.trbear96.qol.core.getTargetBlock
import com.trbear96.qol.core.onTick
import com.trbear96.qol.core.panen
import com.trbear96.qol.core.round
import com.trbear96.qol.core.runTaskLater
import com.trbear96.rute
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.RedstoneBlock
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import kotlin.random.Random

object SawitGameplay {
    var pohon = false
    var final = false

    @JvmField
    var breakingPos: BlockPos? = null;
    var lahan: ClientWorld? = null
    var pos: BlockPos? = null

    fun update_lahan() {
        lahan = client.world
        pos = client.player?.blockPos
    }

    fun cek_lahan(): Boolean {
        val current = client.player!!.blockPos
        if (client.world != lahan ||
            pos?.isWithinDistance(Vec3i(current.x, current.y, current.z), 2.0) != true ||
            pos?.isWithinDistance(Vec3i(current.x, current.y, current.z), .0) == true // TODO
        ) {
            // lahan berubah
            // lakukan sesuati
            // TODO
            return false
        }
        return true
    }

    // initialization
    fun tanamSawit() {
        onTick {
            val player = client.player
            if (player != null) {
                val down = player.blockPos.down()
                val blockState = client.world!!.getBlockState(down)
                val block = blockState.block
                pohon = (block is RedstoneBlock) || (block == Blocks.GRASS_BLOCK)
                final = (block == Blocks.END_STONE) || (block == Blocks.OBSIDIAN)
            }
        }
    }

    var flag = false

    fun panenSawit() {
        breakingPos = null
        onTick {
            if (!panen || client.world == null) {
                it.close()
                println("Berhenti memanen..")
                pohon = false
                rute.berhenti()
                return@onTick
            }
            val player = client.player ?: return@onTick
            update_lahan()

            val berhasil = rute.panen(player)
            if (!berhasil) {
                pohon = false
                rute.berhenti()
                return@onTick
            }
            if (client.crosshairTarget !is BlockHitResult) {
                breakingPos = null
//                return@onTick
            }

            if (jagaenggrek && (player.yaw.round(1) != rute.x || player.pitch.round(1) != rute.y)) {
                siapinEngrek(player)
//                return@onTick
            }
            val hit = client.crosshairTarget as? BlockHitResult
            if (hit == null || client.world!!.isAir(hit.blockPos)) {
                breakingPos = null
            } else if (breakingPos == null) {
                breakingPos = hit.blockPos
                client.interactionManager?.attackBlock(hit.blockPos, player.facing)
            } else if (breakingPos == hit.blockPos) {
                client.interactionManager?.updateBlockBreakingProgress(hit.blockPos, player.facing)
            } else breakingPos = hit.blockPos

            if (pohon && !flag) {
                println("Edge has been reached")
                pohon = false
                flag = true
                rute.tebangPohon(player)
                runTaskLater(120) { flag = false }
            }
            if (final) {
                final = false
                client.player?.networkHandler?.sendChatCommand("warp garden")
            }
            istirahat()
        }
    }

    fun istirahat() {
        if (0.00417f.rollChance()) {
            println("Sedang istirahat... ")
            if (capek)//
                client.player?.sendMessage(
                    Text.literal("[MySawit] ")
                        .styled { it.withBold(true).withColor(Formatting.GREEN) }
                        .append(
                            Text.literal("ngko disek kang, kesel")
                                .styled { style -> style.withBold(false).withColor(Formatting.WHITE) }
                        ), true
                )
            panen = false
            runTaskLater(Random.nextInt(30, 100)) {
                println("Istirahat selesai")
                panen = true
                rute.duid()
                // TODO
            }
        }
    }

    fun Float.rollChance(): Boolean {
        val roll = Random.nextInt(1, 1_000_001) // 0.0001 → 100.0000
        val chance = (this * 10_000).toInt()

        return roll <= chance
    }

    fun siapinEngrek(player: ClientPlayerEntity) {
        val lerp = 0.2f       // speed factor (can increase to 0.3~0.5 for faster)
        val epsilon = 0.3f    // small threshold to snap
        val maxDelta = 7f   // max 5 degrees per tick
        // --- Yaw (horizontal) ---
        var diffYaw = (rute.x - player.yaw + 540) % 360 - 180
        if (kotlin.math.abs(diffYaw) < epsilon) {
            player.yaw = rute.x    // snap if very close
        } else {
            player.yaw += diffYaw.coerceIn(-maxDelta, maxDelta) * lerp
        }

        // --- Pitch (vertical) ---
        val diffPitch = rute.y - player.pitch
        if (kotlin.math.abs(diffPitch) < epsilon) {
            player.pitch = rute.y  // snap if very close
        } else {
            player.pitch += diffPitch.coerceIn(-maxDelta, maxDelta) * lerp
        }

    }
}