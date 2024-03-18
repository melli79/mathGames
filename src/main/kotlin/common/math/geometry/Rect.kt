package common.math.geometry

import kotlin.math.roundToInt

data class Rect(val x0 :Double, val y0 :Double, val dx :Double, val dy :Double) {
    companion object {
        fun of(x0 :Double, y0 :Double, x1 :Double, y1 :Double) =
            Rect(x0, y0, x1 - x0, y1 - y0)
    }

    val y1 = y0+dy
    fun px(x :Double) = ((x-x0)*dx).roundToInt()
    fun py(y :Double) = ((y-y0)*dy).roundToInt()
    fun x(px :Int) = (px/dx)+x0
}