package dynamic

import chaos.MyComponent
import chaos.MyWindow
import common.math.geometry.Rect
import common.math.geometry.x1
import common.math.linReg
import java.awt.Color
import java.awt.Graphics
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.roundToInt

class Mlin(val cs :List<Double> = listOf(3.0, 4.0), val offs :List<Double> = listOf(1.0, 0.0)) : MyComponent() {
    override val title :String ="multi-linear progression"

    data class MlinSequence (val cs :List<Double>, val offs :List<Double>) : Sequence<Double> {
        private val buffer = mutableListOf(1.0) // sorted ascending

        override fun iterator() = MlinIterator(this.copy())

        class MlinIterator(val s :MlinSequence) :Iterator<Double> {
            override fun next() :Double {
                val a = s.buffer.removeFirst()
                for ((c, o) in s.cs.zip(s.offs)) {
                    val next = (c*a).toInt() + o
                    if (next > 0)
                        s.buffer.insertSorted(next)
                }
                return a
            }

            override fun hasNext() = s.buffer.isNotEmpty()

            fun copy() = MlinIterator(s.copy())
        }
    }

    companion object {
        fun computeScale(width :Int, height :Int, range :Rect) :Rect {
            val dx = 0.9*width/range.dx;  val dy = 0.9*height/range.dy
            return Rect(range.x0 -(width/dx-range.dx)/2, range.y1 +(height/dy -range.dy)/2, dx, -dy)
        }
    }

    val sequence = MlinSequence(cs, offs).take(1000).toList()
    private val points = sequence.mapIndexed { i, a -> Pair(ln((i+1).toDouble()), ln(a)) }
    private val regs = linReg(points)
    val m = regs.first; val b = regs.second;  val dm = regs.third; val db = regs.fourth
    val range = Rect.of(points.first().first, points.first().second, points.last().first, points.last().second)
    private lateinit var scale :Rect

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        drawAxes(g, scale)
        drawReg(g, scale)
        g.color = Color.BLACK
        for ((x, y) in points) {
            val px = scale.px(x);  val py = scale.py(y)
            g.fillOval(px-1, py-1, 3,3)
        }
    }

    private fun drawReg(g :Graphics, scale :Rect) {
        val x0 = points.first().first;  val x1 = points.last().first
        g.color = Color.BLUE
        val py0 = scale.py(m*x0 + b);  val py1 = scale.py(m*x1 + b)
        g.drawLine(scale.px(x0), py0, scale.px(x1), py1)
        val px0 = scale.px(0.0)
        g.drawString("ln u = %.2f ln n + %.3f".format(m, b), px0+16, 20)
        g.drawString("m = %.2f±%.2f,  b = %.3f±%.3f".format(m, dm, b, db), px0+16, 36)
    }

    private fun drawAxes(g :Graphics, scale :Rect) {
        g.color = Color.GRAY
        val x0 = scale.px(0.0)
        g.drawLine(x0, 0, x0, height)
        val dy = ln(10.0)
        for (y in 0..(range.y1/dy).roundToInt()) {
            val py = scale.py(y*dy)
            g.drawLine(x0-5, py, x0+5, py)
            g.drawString("%.2g".format(exp(y*dy)), x0-15, py-8)
            val py2 = scale.py(y*dy+ln(2.0))
            g.drawLine(x0-5, py2, x0, py2)
            g.drawString("%.2g".format(2*exp(y*dy)), x0-15, py2-8)
            val py5 = scale.py(y*dy+ln(5.0))
            g.drawLine(x0-5, py5, x0, py5)
            g.drawString("%.2g".format(5*exp(y*dy)), x0-15, py5-8)
        }
        assert(0.0 in range.y0..range.y1)
        val y0 = scale.py(0.0)
        g.drawLine(0, y0, width, y0)
        val dx = ln(10.0)
        for (x in 0..(range.x1()/dx).roundToInt()) {
            val px = scale.px(x*dx)
            g.drawLine(px, y0-5, px, y0+5)
            g.drawString("%.0f".format(exp(x*dx)), px-8, y0+16)
            val px2 = scale.px(x*dx+ln(2.0))
            g.drawLine(px2, y0, px2, y0+5)
            g.drawString("%.0f".format(2*exp(x*dx)), px2-8, y0+16)
            val px5 = scale.px(x*dx+ln(5.0))
            g.drawLine(px5, y0, px5, y0+5)
            g.drawString("%.0f".format(5*exp(x*dx)), px5-8, y0+16)
        }
    }
}

fun main() {
    val w = MyWindow(Mlin())
    w.isVisible = true
}

fun <T :Comparable<T>> MutableList<T>.insertSorted(e :T) :Boolean {
    var beg = 0;  var end = size
    while (beg<end) {
        val mid = (beg+end)/2
        val cmp = get(mid).compareTo(e)
        when {
            cmp < 0 -> beg = mid+1
            cmp >0 -> end = mid
            else -> return false
        }
    }
    add(beg, e)
    return true
}
