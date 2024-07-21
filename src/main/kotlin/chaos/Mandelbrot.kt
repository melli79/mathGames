package chaos

import common.math.geometry.Rect
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class Mandelbrot : MyComponent() {
    override val title = "Mandelbrot"

    companion object {
        const val WRAP = 256u
        val brown = Color(192, 128, 0)
        val purple = Color(192, 0, 192)
        val colors = arrayOf(Color.black, Color.blue, Color.green, Color.cyan, purple, brown, Color.gray)
        val shades = arrayOf(Color.black, Color.red.darker(), Color.red)
        fun colorize(it :Int) :Color = if (it==0) Color.BLACK  else if (it>0) colors[it%(colors.size-1)+1]
          else shades[(-it)%shades.size]

        fun iterate(cx :Double, cy :Double, x0 :Double, y0 :Double, maxIt :UInt) :Int {
            var x=x0;  var y=y0
            for (it in 1u..maxIt) {
                val x2=x*x;  val y2=y*y
                if (x2+y2>=4.0)
                    return it.toInt()
                y = 2*x*y +cy
                x = x2-y2 +cx
            }
            return -((sqrt(x*x+y*y)*6).toInt())
        }

        fun computeScale(width :Int, height :Int, range :Rect) :Rect {
            val dx = min(width/range.dx, height/range.dy)
            return Rect(range.x0 -(width/dx-range.dx)/2, range.y1 +(height/dx -range.dy)/2, dx, -dx)
        }

        val range0 = Rect.of(-2.0, -1.6, 0.5, 1.6)
    }

    enum class Mode {
        Mandel, Julia
    }

    private var range = range0
    private var maxIt = 300u
    private var mode = Mode.Mandel
    private lateinit var scale :Rect

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        if (mode==Mode.Mandel) {
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
        } else {
            val cx = scale.x(width/2);  val cy = scale.y(height/2)
            val dx = max(4.0/width, 3.0/height)
            var y = height/2*dx
            for (py in 0..< height) {
                var x = -width/2*dx
                for (px in 0..< width) {
                    val it = iterate(cx, cy, x, y, maxIt)
                    g.color = colorize(it)
                    g.fillRect(px,py, 1,1)
                    x += dx
                }
                y -= dx
            }
        }
    }

    private fun scaleDown(pt :Point) {
        val dx = 1.2 * range.dx
        val dy = 1.2 * range.dy
        range = Rect(scale.x(pt.x)-dx/2, scale.y(pt.y)-dy/2, dx, dy)
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
            KeyEvent.VK_SPACE, KeyEvent.VK_ENTER -> mode = if (mode == Mode.Julia) Mode.Mandel else Mode.Julia
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
