package chaos

import common.math.geometry.Rect
import common.math.geometry.x1
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import kotlin.math.max
import kotlin.math.sqrt

class Mandelbrot : MyComponent() {
    override val title :String
        get() = mode.getTitle(range)
    fun updateTitle() = window.updateTitle(title)

    companion object {
        val brown = Color(192, 128, 0)
        val purple = Color(192, 0, 192)
        val colors = arrayOf(Color.black, Color.blue, Color.green, Color.cyan, purple, brown, Color.gray)
        val shades = arrayOf(Color.black, Color.red.darker(), Color.red)
        fun colorize(it :Int) :Color = if (it==0) Color.BLACK  else if (it>0) colors[it%(colors.size-1)+1]
          else shades[(-it)%shades.size]
        fun colorize0(it :Int) :Color = if (it<=0) Color.BLACK  else colors[it%(colors.size-1)+1]

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

        fun iterate3(cx :Double, cy :Double, x0 :Double, y0 :Double, maxIt :UInt) :Int {
            var x=x0;  var y=y0
            for (it in 1u..maxIt) {
                val x2=x*x;  val y2=y*y
                if (x2+y2>=4.0)
                    return it.toInt()
                y = (3*x2-y2)*y +cy
                x = (x2-3*y2)*x +cx
            }
            return -((sqrt(x*x+y*y)*6).toInt())
        }

        fun iterate4(cx :Double, cy :Double, x0 :Double, y0 :Double, maxIt :UInt) :Int {
            var x=x0;  var y=y0
            for (it in 1u..maxIt) {
                val x2=x*x;  val y2=y*y
                if (x2+y2>=4.0)
                    return it.toInt()
                y = 4*(x2-y2)*x*y +cy
                x = (y2-6*x2)*y2+x2*x2 +cx
            }
            return -((sqrt(x*x+y*y)*6).toInt())
        }

        fun iterate5(cx :Double, cy :Double, x0 :Double, y0 :Double, maxIt :UInt) :Int {
            var x=x0;  var y=y0
            for (it in 1u..maxIt) {
                val x2=x*x;  val y2=y*y
                if (x2+y2>=4.0)
                    return it.toInt()
                y = ((5*x2-10*y2)*x2 +y2*y2)*y +cy
                x = ((x2-10*y2)*x2 +5*y2*y2)*x +cx
            }
            return -((sqrt(x*x+y*y)*6).toInt())
        }

        fun computeScale(width :Int, height :Int, range :Rect) :Rect {
            val dx = 1.2*max(range.dx/width, range.dy/height)
            return Rect(range.x0 -(dx*width-range.dx)/2, range.y1 +(height*dx -range.dy)/2, dx, -dx)
        }

        val range2 = Rect.of(-2.0, -1.33, 0.5, 1.33)
        val range3 = Rect.of(-1.5, -1.2, 1.2, 1.5)
        val range4 = Rect.of(-1.5, -1.2, 1.0, 1.2)
        val range5 = Rect.of(-1.2, -1.0, 1.2, 1.0)
    }

    enum class Mode {
        Mandel {
            override fun getTitle(range :Rect) = "$name %.2f..%.2f x %.2f..%.2f".format(range.x0, range.x1(), range.y0, range.y1)
        }, Julia {
            override fun getTitle(range :Rect) = "$name %.4f %+.4fI".format(range.x0+range.dx/2, range.y0+range.dy/2)
        };

        abstract fun getTitle(range :Rect) :String
    }

    var range0 = range2
        private set
    var range = range0
        private set
    var maxIt = 300u
        private set
    var mode = Mode.Mandel
        private set
    var deg = 2.toUByte()
        private set
    private lateinit var scale :Rect

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        updateTitle()
        if (mode==Mode.Mandel) {
            var cy = scale.y(0)
            for (py in 0..<height) {
                var cx = scale.x(0)
                for (px in 0..<width) {
                    val it = when (deg.toInt()) {
                        3 -> iterate3(cx, cy, 0.0, 0.0, maxIt)
                        4 -> iterate4(cx, cy, 0.0, 0.0, maxIt)
                        5 -> iterate5(cx, cy, 0.0, 0.0, maxIt)
                        else -> iterate(cx, cy, 0.0, 0.0, maxIt)
                    }
                    g.color = colorize(it)
                    g.drawRect(px, py, 1, 1)
                    cx += scale.dx
                }
                cy += scale.dy
            }
        } else {
            val cx = scale.x1(width/2);  val cy = scale.y1(height/2)
            scale = computeScale(width, height, range3)
            var y = scale.y0
            for (py in 0..< height) {
                var x = scale.x0
                for (px in 0..< width) {
                    val it = when (deg.toInt()) {
                        3 -> iterate3(cx, cy, x, y, maxIt)
                        4 -> iterate4(cx, cy, x, y, maxIt)
                        5 -> iterate5(cx, cy, x, y, maxIt)
                        else -> iterate(cx, cy, x, y, maxIt)
                    }
                    g.color = colorize0(it)
                    g.fillRect(px,py, 1,1)
                    x += scale.dx
                }
                y += scale.dy
            }
        }
    }

    private fun scaleDown(pt :Point) {
        val dx = 1.2 * range.dx
        val dy = 1.2 * range.dy
        range = if (dx>range0.dx || dy>range0.dy)
            range0
          else
            Rect(scale.x1(pt.x)-dx/2, scale.y1(pt.y)-dy/2, dx, dy)
        repaint()
    }

    private fun scaleUp(pt :Point) {
        val dx = 0.8 * range.dx
        val dy = 0.8 * range.dy
        val x = scale.x1(pt.x)
        val y = scale.y1(pt.y)
        println("%.3g%+.3g @$maxIt".format(x, y))
        range = Rect(x -dx/2, y -dy/2, dx, dy)
        repaint()
    }

    override fun mouseClicked(event :MouseEvent) {
        val pt = event.point
        pt.translate(0, -35)
        if (event.button == MouseEvent.BUTTON1) {
            scaleUp(pt)
        } else {
            scaleDown(pt)
        }
    }

    override fun keyPressed(event :KeyEvent) {
        when (event.keyCode) {
            KeyEvent.VK_SPACE -> mode = if (mode == Mode.Julia) Mode.Mandel else Mode.Julia
            KeyEvent.VK_ENTER -> scaleUp(Point(width/2, height/2))
            KeyEvent.VK_BACK_SPACE -> scaleDown(Point(width/2, height/2))
            KeyEvent.VK_2 -> {
                deg = 2.toUByte()
                range0 = range2
            }
            KeyEvent.VK_3 -> {
                deg = 3.toUByte()
                range0 = range3
            }
            KeyEvent.VK_4 -> {
                deg = 4.toUByte()
                range0 = range4
            }
            KeyEvent.VK_5 -> {
                deg = 5.toUByte()
                range0 = range5
            }
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

fun main() {
    val window = MyWindow(Mandelbrot())
    window.isVisible = true
}

fun Rect.x1(px :Int) = px*dx +x0
fun Rect.y1(py :Int) = py*dy +y0
