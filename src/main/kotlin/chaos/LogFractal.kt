package chaos

import common.math.Complex
import common.math.geometry.Rect
import common.math.log
import probability.cb
import java.awt.Color
import java.awt.Graphics
import java.util.Random
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.roundToLong

var random = Random(System.currentTimeMillis())

class LogFractal :MyComponent() {
    override val title = "Log Frractal"

    val range = Rect.of(-1.0, -1.6, 5.0, 1.6)
    private lateinit var scale :Rect

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        var z = Complex(0.5,0.5)
        for(n in 0..10) {
            z = log(z+ Complex.I*(2*PI*(random.nextGaussian().roundToLong())))
        }
        g.color = Color.BLACK
        for (n in 1..100_000) {
            g.fillRect(scale.px(z.re)-1, scale.py(z.im)-1, 3, 3)
            z = log(z+ Complex.I*(2*PI*(cb(random.nextGaussian()*3).roundToLong())))
        }
    }

    private fun computeScale(width :Int, height :Int, range :Rect) :Rect {
        val dx = 0.9*min(width/range.dx, height/range.dy)
        return Rect(range.x0 -(width/dx-range.dx)/2, range.y1 +(height/dx -range.dy)/2, dx, -dx)
    }
}
