package chaos

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JComponent
import javax.swing.JFrame
import kotlin.math.roundToInt
import kotlin.math.tan
import kotlin.system.exitProcess

class SumTanN :JComponent() {
    private var limit = 16u
    private var points :List<Point> = emptyList()

    fun scaleUp() {
        if (limit<UInt.MAX_VALUE/2u)
            limit *= 2u
        repaint()
    }

    fun scaleDown() {
        if (limit>128u)
            limit /= 2u
        repaint()
    }

    override fun paint(g :Graphics) {
        if (points.size.toUInt()<limit)
            points = computePoints()
        val range = computeRange()
        val scale = computeScale(range, width, height)
        drawAxes(g, scale)
        g.color = Color.BLACK
        var lastX = scale.px(points[0].x);  var lastY = scale.py(points[0].y)
        for (p in points) {
            val px = scale.px(p.x);  val py = scale.py(p.y)
            g.drawLine(lastX,lastY, px,py)
            lastX = px;  lastY = py
        }
    }

    private fun drawAxes(g :Graphics, scale :Rect) {
        g.color = Color.GRAY
        val x0 = scale.px(0.0)
        g.drawLine(x0, 0, x0, height-1)
        val y0 = scale.py(0.0)
        g.drawLine(0,y0, width-1, y0)
    }

    private fun computeScale(range :Rect, width :Int, height :Int) =
        Rect(range.x0, range.y1, width/range.dx, -height/range.dy)

    private fun computeRange() :Rect {
        val xm = 0.0;   val xM = limit.toDouble()
        val consideredPoints = points.subList(0, limit.toInt())
        val ym = consideredPoints.minOf { p -> p.y };  val yM = consideredPoints.maxOf { p -> p.y }
        val dx = xM-xm;  val dy = yM-ym
        return Rect(xm-0.05*dx, ym-0.05*dy, dx*1.1, dy*1.1)
    }

    private fun computePoints() :List<Point> {
        var sum = 0.0
        val result = mutableListOf(Point(0.0, sum))
        for (n in 1u..limit) {
            sum += tan(n.toDouble())
            result.add(Point(n.toDouble(), sum))
        }
        return result
    }
}

data class Point(val x :Double, val y :Double) {}

data class Rect(val x0 :Double, val y0 :Double, val dx :Double, val dy :Double) {
    companion object {
        fun of(x0 :Double, y0 :Double, x1 :Double, y1 :Double) =
            Rect(x0, y0, x1-x0, y1-y0)
    }

    val y1 = y0+dy
    fun px(x :Double) = ((x-x0)*dx).roundToInt()
    fun py(y :Double) = ((y-y0)*dy).roundToInt()
}

class MyWindow(val content :SumTanN) :JFrame(), KeyListener {
    init {
        this.layout = BorderLayout()
        this.contentPane = content
        defaultCloseOperation = EXIT_ON_CLOSE
        addKeyListener(this)
        setSize(800, 600)
    }

    override fun keyTyped(event :KeyEvent) = when (event.keyChar) {
        '+', '=' -> content.scaleUp()
        '-' -> content.scaleDown()
        'q', 'Q', 'x', 'X' -> exitProcess(0)
        else -> {}
    }

    override fun keyPressed(event :KeyEvent?) {}

    override fun keyReleased(event :KeyEvent?) {}
}

fun main() {
    val window = MyWindow(SumTanN())
    window.isVisible = true
}
