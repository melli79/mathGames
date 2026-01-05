package geometry

import chaos.MyComponent
import chaos.MyWindow
import chaos.computeScale
import com.google.common.collect.BoundType
import com.google.common.collect.Range.range
import common.math.geometry.Rect
import common.math.sgn
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import kotlin.math.*

class Spiral() : MyComponent() {
    override val title = "Spiral r = 3 + sgn(phi)*ln(|phi|+1)"

    val phiRange = range(-4*PI, BoundType.CLOSED,4*PI, BoundType.CLOSED)
    val range :Rect
    private lateinit var scale :Rect

    init {
        val r = 6.0
        range = Rect.of(-r, -r, r, r)
    }

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        val dphi = 10.0/width
        g.drawAxes()
        g.color = Color.green.darker()
        var phi = phiRange.lowerEndpoint()
        val phiEnd = phiRange.upperEndpoint() + dphi/2
        var r = 3.0 + sgn(phi)*ln(abs(phi)+1.0)
        var lastPx = scale.px(r*cos(phi));  var lastPy = scale.py(r*sin(phi))
        do {
            r = 3.0 + sgn(phi)*ln(abs(phi)+1.0)
            val px = scale.px(r*cos(phi))
            val py = scale.py(r*sin(phi))
            g.drawLine(lastPx-1, lastPy-1, px-1, py-1)
            g.drawLine(lastPx, lastPy, px, py)
            g.drawLine(lastPx+1, lastPy+1, px+1, py+1)
            lastPx = px;  lastPy = py
            phi += dphi
        } while (phi <= phiEnd)
        g.color = Color.black
        g.font = font.deriveFont(Font.BOLD, 24.0f)
        g.drawString(title, 50, 75)
    }

    private fun Graphics.drawAxes() {
        color = Color.gray
        val px0 = scale.px(0.0)
        drawLine(px0, 0, px0, height)
        val py0 = scale.py(0.0)
        drawLine(0, py0, width, py0)
        val dr = 1.0
        for (r1 in 0..(6.0/dr).roundToInt()) {
            val r = r1*dr
            val px = scale.px(-r)
            val py = scale.py(r)
            drawOval(px, py, (2*r*scale.dx).roundToInt(), abs(2*r*scale.dy).roundToInt())
            drawString("%.1f".format(r), scale.px(r)-24, py0-5)
        }
    }
}

fun main() {
    val w = MyWindow(Spiral())
    w.isVisible = true
}
