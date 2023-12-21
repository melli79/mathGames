package chaos

import common.math.geometry.Point2D
import common.math.geometry.Rect
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.math.tan
import kotlin.system.exitProcess

class SumTanN :JComponent() {
    private var limit = 16u
    private var points :List<Point2D> = emptyList()
    private var range = Rect.of(0.0, -10.0, limit.toDouble(), 1.0)

    init {
        computePoints()
    }

    fun scaleUp() {
        if (limit<UInt.MAX_VALUE/2u)
            limit *= 2u
        if (limit>points.size.toUInt())
            SwingUtilities.invokeLater { computePoints() }
        else {
            this.range = computeRange()
            repaint()
        }
    }

    fun scaleDown() {
        if (limit>=32u)
            limit /= 2u
        this.range = computeRange()
        repaint()
    }

    override fun paint(g :Graphics) {
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

    private fun computePoints() {
        var sum = 0.0
        val result = mutableListOf(Point2D(0.0, sum))
        for (n in 1u..limit) {
            sum += tan(n.toDouble())
            result.add(Point2D(n.toDouble(), sum))
        }
        this.points = result
        this.range = computeRange()
        repaint()
    }
}

class MyWindow(val content :SumTanN) :JFrame(), KeyListener {
    init {
        this.layout = BorderLayout()
        this.contentPane = content
        defaultCloseOperation = EXIT_ON_CLOSE
        addKeyListener(this)
        setSize(800, 600)
    }

    override fun keyTyped(event :KeyEvent) {/* not needed */}

    override fun keyPressed(event :KeyEvent) {/* not needed */}

    override fun keyReleased(event :KeyEvent) = when (event.keyChar) {
        '+', '=' -> content.scaleUp()
        '-' -> content.scaleDown()
        'q', 'Q', 'x', 'X' -> exitProcess(0)
        else -> {/* ignore */}
    }
}

fun main() {
    val window = MyWindow(SumTanN())
    window.isVisible = true
}
