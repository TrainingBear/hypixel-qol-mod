package com.trbear96.qol.perkembangan_teknologi

import com.trbear96.client
import com.trbear96.qol.core.onTick
import com.trbear96.qol.core.panen
import com.trbear96.qol.core.round
import net.minecraft.block.Block
import net.minecraft.block.FluidBlock
import net.minecraft.block.RedstoneBlock
import net.minecraft.block.Waterloggable
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.fluid.WaterFluid
import net.minecraft.text.Text
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import kotlin.math.atan2
import kotlin.math.sqrt

abstract class RuteSawit(val x: Float,
                         val y: Float,
                         val speed: Int) {
    fun berhenti(){
        client.options.attackKey.isPressed = false
        client.options.forwardKey.isPressed = false
        client.options.backKey.isPressed = false
        client.options.leftKey.isPressed = false
        client.options.rightKey.isPressed = false
    }
    abstract fun tebangPohon(player: ClientPlayerEntity)
    abstract fun panen(player: ClientPlayerEntity) : Boolean

    private fun siapkanEngrek(player: ClientPlayerEntity){
        val lerp = 0.2f       // speed factor (can increase to 0.3~0.5 for faster)
        val epsilon = 0.3f    // small threshold to snap
        val maxDelta = 7f   // max 5 degrees per tick

        // --- Yaw (horizontal) ---
        var diffYaw = (x - player.yaw + 540) % 360 - 180
        if (kotlin.math.abs(diffYaw) < epsilon) {
            player.yaw = x    // snap if very close
        } else {
            player.yaw += diffYaw.coerceIn(-maxDelta, maxDelta) * lerp
        }

        // --- Pitch (vertical) ---
        val diffPitch = y - player.pitch
        if (kotlin.math.abs(diffPitch) < epsilon) {
            player.pitch = y  // snap if very close
        } else {
            player.pitch += diffPitch.coerceIn(-maxDelta, maxDelta) * lerp
        }

    }

    fun setupEngrek(ac: () -> Unit) {
        onTick {
            if (!panen) {
                it.close()
                return@onTick
            }
            val player = client.player ?: run {
                it.close()
                return@onTick
            }

            if (player.yaw.round(1) != x || player.pitch.round(1) != y) {
                siapkanEngrek(player)
            } else {
                it.close()
                ac.invoke()
                println("Memanen...")
            }
        }
    }

    fun duid(){
        setupEngrek {
            SawitGameplay.panenSawit()
        }
    }

    object topaz : RuteSawit(.0f, -58.5f, 400) {
        var kanan: Boolean? = true
        override fun tebangPohon(player: ClientPlayerEntity) {
            val left = player.blockPos.east(1)
            val right = player.blockPos.west(1)
            kanan = if (client.world!!.getBlockState(right).block is FluidBlock) false
            else if (client.world!!.getBlockState(left).block is FluidBlock) true
            else null
        }

        override fun panen(player: ClientPlayerEntity): Boolean {
            if(kanan == null) return false
//            client.options.attackKey.isPressed = true
            client.options.forwardKey.isPressed = true
            client.options.leftKey.isPressed = kanan!!
            client.options.rightKey.isPressed = !kanan!!
            return true
        }
    }

    object dumpy :  RuteSawit(-164.0f, 5.5f, 233) {
        var kanan: Boolean? = true
        override fun tebangPohon(player: ClientPlayerEntity) {
            val right = player.blockPos.east(1)
            val left = player.blockPos.west(1)
            kanan = if (client.world!!.getBlockState(right).block is FluidBlock) false
            else if (client.world!!.getBlockState(left).block is FluidBlock) true
            else null
        }

        override fun panen(player: ClientPlayerEntity): Boolean {
            if(kanan == null) return false
            client.options.leftKey.isPressed = kanan!!
            client.options.rightKey.isPressed = !kanan!!
            return true
        }

    }

    object yangambi : RuteSawit(0f, 0f, 248) {
        var kanan: Boolean? = true
        override fun tebangPohon(player: ClientPlayerEntity) {
            val left = player.blockPos.east(1)
            val right = player.blockPos.west(1)
            kanan = if (client.world!!.getBlockState(right).block is FluidBlock) false
            else if (client.world!!.getBlockState(left).block is FluidBlock) true
            else null
        }

        override fun panen(player: ClientPlayerEntity): Boolean {
            if(kanan == null) return false
//            client.options.attackKey.isPressed = true
            client.options.forwardKey.isPressed = true
            client.options.leftKey.isPressed = kanan!!
            client.options.rightKey.isPressed = !kanan!!
            return true
        }
    }
}
// Dumpy (SP-I) = mushroom
// Yangambi = wheat, dan semacamnya
// Langkat,
// SP540,
// 540_NG,
// DelixPisifera(DXP)
// Topaz = melon, pumpkin
// AAL_Sejahtera,
// Nirmala,
// Lestari,
// Sriwijaya_2,
// Sriwijaya_4
