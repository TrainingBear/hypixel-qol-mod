package com.trbear96.qol.core

import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.round(decimals: Int): Float {
    val factor = 10.0.pow(decimals.toDouble()).toFloat()
    return (this * factor).roundToInt() / factor
}