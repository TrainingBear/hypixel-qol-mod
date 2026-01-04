package com.trbear96.qol.core

import com.trbear96.client
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import kotlin.math.atan2
import kotlin.math.sqrt

fun lookAt(targetPos: Vec3d) {
    val player = client.player?: return
    val dx = targetPos.x - player.x
    val dy = targetPos.y - (player.y + player.eyeY)
    val dz = targetPos.z - player.z

    val distanceXZ = sqrt(dx * dx + dz * dz)
    val yaw = Math.toDegrees(atan2(-dx, dz)).toFloat()
    val pitch = Math.toDegrees(atan2(-dy, distanceXZ)).toFloat()

    player.yaw = yaw
    player.pitch = pitch
}

fun getTargetBlock(): BlockHitResult {
    val player = client.player!!
    val reach = 5.0
    val cameraPos = player.eyePos
    val lookVec = player.rotationVector
    val targetPos = cameraPos.add(lookVec.multiply(reach))

    return client.world!!.raycast(
        RaycastContext(
            player.eyePos,   // start
            targetPos,       // end
            RaycastContext.ShapeType.COLLIDER,
            RaycastContext.FluidHandling.ANY,
            player
        )
    ) as BlockHitResult
}