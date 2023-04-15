package probability

import chaos.Rect
import common.Quadruple
import common.math.epsilon
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Graphics
import javax.swing.JComponent
import javax.swing.JFrame
import kotlin.math.ln
import kotlin.math.min

fun main() {
    val thresholds = mutableMapOf<UInt, Triple<Double, Double, Double>>()
    var n = 4u
    while (n<1048000u) {
        thresholds[n] = Triple(findThreshold(n, 0.05), findThreshold(n), findThreshold(n, 0.95))
        n += n/4u
    }
    val points = thresholds.entries.map { Quadruple(ln(it.key.toDouble()), ln(it.value.first), ln(it.value.second), ln(it.value.third)) }
    val mb = linReg(points)
    val window = MyWindow(Plotter(points, mb))
    window.isVisible = true
}

fun linReg(points: List<Quadruple<Double, Double, Double, Double>>) :Pair<Double, Double> {
    val n = points.sumOf { it.first }
    val x = points.sumOf { it.first*it.first }/n
    val x2_xx = points.sumOf { cb(it.first) }/n -x*x
    assert(x2_xx > 5*epsilon)
    val f = 1/x2_xx
    val y1 = points.sumOf { it.second*it.first }/n
    val xy1 = points.sumOf { it.first*it.second*it.first }/n
    val m1 = (xy1 - x*y1) * f
    val b1 = y1 - m1*x
    println("ln t_05 = %.5f*ln N %+.5f".format(m1, b1))
    val y2 = points.sumOf { it.third*it.first }/n
    val xy2 = points.sumOf { it.first*it.third*it.first }/n
    val m2 = (xy2 - x*y2) * f
    val b2 = y2 - m2*x
    println("ln t_50 = %.5f*ln N %+.5f".format(m2, b2))
    val y3 = points.sumOf { it.fourth*it.first }/n
    val xy3 = points.sumOf { it.first*it.fourth*it.first }/n
    val m3 = (xy3 - x*y3) * f
    val b3 = y3 - m3*x
    println("ln t_95 = %.5f*ln N %+.5f".format(m3, b3))
    return Pair(m2, b2)
}

fun cb(x :Double) = x*x*x

class Plotter(private val points: List<Quadruple<Double, Double, Double, Double>>, private val linReg: Pair<Double, Double>) :JComponent() {
    private val range = computeRange(points)

    private fun computeRange(points: List<Quadruple<Double, Double, Double, Double>>) :Rect {
        val first = points.first()
        var xm = first.first; var xM = xm
        var ym = first.second;  var yM = ym
        for (p in points) {
            if (p.first<xm)
                xm = p.first
            else if (xM<p.first)
                xM = p.first
            if (p.second<ym)
                ym = p.second
            else if (yM<p.second)
                yM = p.second
            if (p.third<ym)
                ym = p.third
            else if (yM<p.third)
                yM = p.third
            if (p.fourth<ym)
                ym = p.fourth
            else if (yM<p.fourth)
                yM = p.fourth
        }
        return Rect.of(min(xm, -0.5), min(ym, -0.5), xM, yM)
    }

    private lateinit var scale :Rect

    override fun paint(g :Graphics) {
        scale = computeScale(width, height)
        g.drawAxes()
        var lastX = scale.px(points.first().first)
        var lastY1 = scale.py(points.first().second)
        var lastY3 = scale.py(points.first().fourth)
        for (p in points) {
            val px = scale.px(p.first);  val py1 = scale.py(p.second)
            g.color = Color.GREEN
            g.drawLine(lastX,lastY1, px,py1)
            val py2 = scale.py(p.third)
            g.color = Color.BLACK
            g.fillOval(px-2, py2-2, 5,5)
            val py3 = scale.py(p.fourth)
            g.color = Color.RED
            g.drawLine(lastX, lastY3, px, py3)
            lastX = px;  lastY1 = py1;  lastY3 = py3
        }
        g.color = Color.BLUE
        val y0 = linReg.first*range.x0 +linReg.second;  val y1 = linReg.first*range.x1() +linReg.second
        g.drawLine(scale.px(range.x0), scale.py(y0), scale.px(range.x1()), scale.py(y1))
    }

    private fun Graphics.drawAxes() {
        color = Color.GRAY
        val x0 = scale.px(0.0)
        drawLine(x0,0, x0,height)
        val y0 = scale.py(0.0)
        drawString("0", x0-10,y0+14)
        val x1 = scale.px(1.0)
        drawLine(x1, y0, x1,y0+5)
        drawString("1.0", x1-10,y0+16)
        val x10 = scale.px(10.0)
        drawLine(x10, y0, x10,y0+5)
        drawString("10.0", x10-16,y0+16)

        drawLine(0,y0, width,y0)
        val y1 = scale.py(1.0)
        drawLine(x0,y1, x0+5,y1)
        drawString("1.0", x0-24,y1+4)
        val y10 = scale.py(10.0)
        drawLine(x0,y10, x0+5,y10)
        drawString("10.0", x0-32,y10+4)
    }

    private fun computeScale(width: Int, height: Int): Rect {
        val dx = min(width/range.dx, height/range.dy)*0.95
        return Rect(range.x0 -(width/dx-range.dx)/2, range.y1 +(height/dx-range.dy), dx, -dx)
    }
}

fun Rect.x1() = x0+dx

class MyWindow(rootPane :JComponent) : JFrame("Birthday Paradox") {
    init {
        layout = BorderLayout()
        contentPane = rootPane
        setSize(800, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
    }
}
