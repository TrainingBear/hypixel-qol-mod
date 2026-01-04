package com.trbear96.qol.perkembangan_teknologi

import com.trbear96.client
import net.minecraft.block.FluidBlock
import net.minecraft.block.Waterloggable
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.fluid.WaterFluid

abstract class RuteSawit(x: Double, y: Double, speed: Int) {
    fun berhenti(){
        client.options.forwardKey.isPressed = false
        client.options.backKey.isPressed = false
        client.options.leftKey.isPressed = false
        client.options.rightKey.isPressed = false
    }
    abstract fun tebangPohon(player: ClientPlayerEntity)
    abstract fun panen(player: ClientPlayerEntity) : Boolean

    fun siapkanEngrek(player: ClientPlayerEntity){
//        player.
    }

    object topaz : RuteSawit(.0, -58.5, 400) {
        var kanan: Boolean? = true
        override fun tebangPohon(player: ClientPlayerEntity) {
            client.options.leftKey.isPressed = false
            client.options.rightKey.isPressed = false

            val left = player.blockPos.east()
            val right = player.blockPos.west()
            kanan = if (client.world!!.getBlockState(right).block is FluidBlock) true
            else if (client.world!!.getBlockState(left).block is FluidBlock) true
            else null
        }

        override fun panen(player: ClientPlayerEntity): Boolean {
            if(kanan == null) return false
            if(kanan!!) panenKanan()
            else panenKiri()
            return true
        }

        fun panenKanan() {
            client.options.forwardKey.isPressed = false
            client.options.leftKey.isPressed = true
            client.options.rightKey.isPressed = false
        }
        fun panenKiri() {
            client.options.forwardKey.isPressed = false
            client.options.leftKey.isPressed = false
            client.options.rightKey.isPressed = true
        }

    }

    object dumpy : RuteSawit(-164.0, 5.5, 233) {
        override fun tebangPohon(player: ClientPlayerEntity) {
            TODO("Not yet implemented")
        }

        override fun panen(player: ClientPlayerEntity): Boolean {
            TODO("Not yet implemented")
        }
    }
}
// Dumpy (SP-I),
// Yangambi,
// Langkat,
// SP540,
// 540_NG,
// DelixPisifera(DXP)
// Topaz, = melon
// AAL_Sejahtera,
// Nirmala,
// Lestari,
// Sriwijaya_2,
// Sriwijaya_4
