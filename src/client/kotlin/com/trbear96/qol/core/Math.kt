package com.trbear96.qol.core

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.round(decimals: Int): Float {
    return BigDecimal(this.toDouble())
        .setScale(decimals, RoundingMode.HALF_DOWN) // truncate, do NOT round
        .toFloat()
}