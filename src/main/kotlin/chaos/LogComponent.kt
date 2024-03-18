package chaos

import common.math.geometry.Rect
import java.awt.Color
import java.awt.Graphics
import kotlin.math.abs
import kotlin.math.ln

class LogComponent(xrange :ClosedRange<Double> = (1e-3) ..1.0) : MyComponent() {
    companion object {
        val maxIt = 20

        private fun computeScale(range :Rect, width :Int, height :Int) :Rect {
            val dx = width/range.dx;  val dy = height/range.dy
            return Rect(range.x0, range.y1, dx, -dy)
        }

        fun colorize(n :Int) :Color {
            val offset = 255*n / maxIt
            return Color(255-offset, 0, offset)
        }
    }

    val range = Rect(xrange.start, -2.0, xrange.endInclusive, 3.0)
    private lateinit var scale :Rect

    override val title = "Log iterations"

    override fun paint(g :Graphics) {
        scale = computeScale(range, width, height)
        for (px in 0..< width) {
            var y = scale.x(px)
            for (n in 0..maxIt) {
                val py = scale.py(y)
                g.color = colorize(n)
                g.fillRect(px-1,py-1, 3,3)
                y = ln(abs(y))
            }
        }
    }
}
