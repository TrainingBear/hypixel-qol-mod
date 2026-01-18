package com.trbear96.qol.perkembangan_teknologi

import com.trbear96.capek
import com.trbear96.client
import com.trbear96.conceal
import com.trbear96.jagaenggrek
import com.trbear96.qol.core.TickScheduler.scheduleTimer
import com.trbear96.qol.core.onTick
import com.trbear96.qol.core.panen
import com.trbear96.qol.core.currentTick
import com.trbear96.qol.core.runTaskLater
import com.trbear96.rute
import net.minecraft.block.Blocks
import net.minecraft.block.RedstoneBlock
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.registry.RegistryKey
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World
import kotlin.random.Random

object SawitGameplay {
    var pohon = false
    var final = false
    var jarakWatDokAktif = true

    @JvmField
    var breakingPos: BlockPos? = null
    var lahan: RegistryKey<World>? = null
    var pos: BlockPos? = null


    /**
     * @return false jika lahan berubah, true jika tidak
     */
    fun cek_lahan(): Boolean {
        if (!panen || client.player == null) return false
        val cur: BlockPos = client.player!!.blockPos
        val interfal = if (conceal) currentTick % 10 == 0 else false // .5 demtik
        lahan = client.world?.registryKey
        if (client.world?.registryKey != lahan || (jarakWatDokAktif && interfal && !(pos?.isWithinDistance(
                Vec3i(cur.x, cur.y, cur.z), 8.0
            ) ?: true))
        ) {
            client.player!!.sendMessage(
                Text.literal("[MySawit] ").styled { t -> t.withBold(true).withColor(Formatting.GREEN) }
                    .append(
                        Text.literal("Bahaya antek-antek asing mengintai")
                            .styled { style -> style.withBold(false).withColor(Formatting.RED) }), false
            )
            var count = 1
            scheduleTimer(1, 2) { w ->
                if (count > 15) {
                    w.close()
                    return@scheduleTimer
                }
                client.player?.playSound(SoundEvents.ITEM_TOTEM_USE, 10F, 1.2F)
                count += 1
            }

            return false
        }
        if (interfal) pos = client.player!!.blockPos
        return true
    }

    var flag = false

    fun panenSawit() {
        lahan = client.world?.registryKey
        pos = client.player?.blockPos
        jarakWatDokAktif = true
        breakingPos = null
        onTick {
            if (!cek_lahan()) {
                it.close()
                println("Berhenti memanen..")
                pohon = false
                rute.berhenti()
                return@onTick
            }
            val player = client.player!!
            val berhasil = rute.panen(player)
            if (!berhasil) {
                pohon = false
                rute.berhenti()
                return@onTick
            }
            if (jagaenggrek && (player.yaw != rute.x || player.pitch != rute.y))
                siapinEngrek(player)
            if (client.crosshairTarget !is BlockHitResult)
                breakingPos = null

            val hit = client.crosshairTarget as? BlockHitResult
            if (hit == null || client.world!!.isAir(hit.blockPos))
                breakingPos = null
            else if (breakingPos == null) {
                breakingPos = hit.blockPos
                client.interactionManager?.attackBlock(hit.blockPos, player.facing)
            } else if (breakingPos == hit.blockPos)
                client.interactionManager?.updateBlockBreakingProgress(hit.blockPos, player.facing)
            else breakingPos = null

            serlokTakParani(player)
            if (pohon && !flag) {
                println("Edge has been reached")
                pohon = false; flag = true
                rute.tebangPohon(player)
                runTaskLater(120) { flag = false }
            }
            if (final) {
                final = false
                runTaskLater(240) {
                    pos = client.player!!.blockPos
                    jarakWatDokAktif = true
                }
                client.player?.networkHandler?.sendChatCommand("warp garden")
            }
            istirahat()
        }
    }

    fun istirahat() {
        if (0.00417f.rollChance()) {
            println("Sedang istirahat... ")
            belIstirahat(true)
            pos = client.player!!.blockPos
            panen = false
            runTaskLater(Random.nextInt(30, 100)) {
                println("Istirahat selesai")
                belIstirahat(false)
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
        val diffYaw = (rute.x - player.yaw + 540) % 360 - 180
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

    fun serlokTakParani(player: ClientPlayerEntity) {
        val block = client.world!!.getBlockState(player.blockPos.down()).block
        val left = client.world!!.getBlockState(player.blockPos.east(1)).block
        val right = client.world!!.getBlockState(player.blockPos.west(1)).block
        pohon = (block is RedstoneBlock) || (block == Blocks.GRASS_BLOCK)
                || (left == Blocks.QUARTZ_BLOCK) || (right == Blocks.QUARTZ_BLOCK)
        final = (block == Blocks.END_STONE) || (block == Blocks.OBSIDIAN)
                || (left == Blocks.END_STONE) || (right == Blocks.END_STONE)
        jarakWatDokAktif = !final
    }

    fun belIstirahat(mulai: Boolean) {
        val pitcekAwal: Float = if (mulai) 1F else 0.7F
        val pitcekAkhir: Float = if (mulai) 0.7F else 1F
        val p = client.player!!

        if (capek) p.sendMessage(
            Text.literal("[MySawit] ").styled { t -> t.withBold(true).withColor(Formatting.GREEN) }.append(
                Text.literal("ngko disek kang, kesel")
                    .styled { style -> style.withBold(false).withColor(Formatting.WHITE) }), false
        )
        p.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), SoundCategory.MASTER, 10F, pitcekAwal)
        p.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), SoundCategory.MASTER, 10F, pitcekAwal)
        runTaskLater(8) {
            p.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), SoundCategory.MASTER, 10F, pitcekAkhir)
            p.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_HARP.value(), SoundCategory.MASTER, 10F, pitcekAkhir)
        }
        var count = 1 // PEREDAM SUARA (RAKYAT)
        scheduleTimer(1, 1) {
            if (count > 9) {
                it.close()
                return@scheduleTimer
            }
            client.soundManager.stopSounds(
                    Identifier.of(
                        "minecraft", "entity.experience_orb.pickup"
                    ), null
                )
            count += 1

        }
    }
}