package chaos

import common.math.Complex
import common.math.geometry.Rect
import common.math.tan
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import kotlin.math.min

class TanMandel : MyComponent() {
    override val title = "z_{n+1} = z_n * tan z_n"

    companion object {
        const val WRAP = 256u
        fun colorize(it :UInt) :Color = if (it==0u) Color.BLACK  else Color((it%WRAP).toInt(),0, 255-(it%WRAP).toInt())

        fun iterate(cx :Double, cy :Double, x0 :Double, y0 :Double, maxIt :UInt) :UInt {
            val c = Complex(cx, cy)
            var z = Complex(x0, y0)
            for (it in 1u..maxIt) {
                if (z.isnan() || z.abs2()>=100.0)
                    return it
                z = z*tan(z) +c
            }
            return 0u
        }

        fun computeScale(width :Int, height :Int, range :Rect) :Rect {
            val dx = min(width/range.dx, height/range.dy)
            return Rect(range.x0 -(width/dx-range.dx)/2, range.y1 +(height/dx -range.dy)/2, dx, -dx)
        }

        val range0 = Rect.of(-8.0, -6.0, 8.0, 6.0)
    }

    private var range = range0
    private var maxIt = 300u
    private lateinit var scale :Rect

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        var cy = scale.y(0)
        for (py in 0..<height) {
            var cx = scale.x(0)
            for (px in 0..<width) {
                val it = iterate(cx, cy, 0.0, 0.0, maxIt)
                g.color = colorize(it)
                g.drawRect(px, py, 1, 1)
                cx += 1 / scale.dx
            }
            cy += 1 / scale.dy
        }
    }

    private fun scaleDown(pt :Point) {
        val dx = 1.2 * range.dx
        val dy = 1.2 * range.dy
        val x = scale.x(pt.x)
        val y = scale.y(pt.y)
        println("%.3g%+.3g @$maxIt".format(x, y))
        range = Rect(x -dx/2, y -dy/2, dx, dy)
        repaint()
    }

    private fun scaleUp(pt :Point) {
        val dx = 0.8 * range.dx
        val dy = 0.8 * range.dy
        val x = scale.x(pt.x)
        val y = scale.y(pt.y)
        println("%.3g%+.3g @$maxIt".format(x, y))
        range = Rect(x -dx/2, y -dy/2, dx, dy)
        repaint()
    }

    override fun mouseClicked(event :MouseEvent) {
        val pt = event.point
        if (event.button == MouseEvent.BUTTON1) {
            scaleUp(pt)
        } else {
            scaleDown(pt)
        }
    }

    override fun keyPressed(event :KeyEvent) {
        when (event.keyCode) {
            KeyEvent.VK_BACK_SPACE -> range = range0
        }
        repaint()
    }

    override fun scaleUp() {
        maxIt += 50u
        print("@$maxIt, ");  System.out.flush()
        repaint()
    }

    override fun scaleDown() {
        maxIt -= 50u
        if (maxIt < 50u)
            maxIt = 50u
        print("@$maxIt, ");  System.out.flush()
        repaint()
    }
}
