package chaos

import common.math.RangeIterator
import common.math.geometry.Rect
import common.math.geometry.x1
import common.math.sqr
import java.awt.Color
import java.awt.Graphics
import kotlin.math.*

class RealLog(private val steps :UInt = 1024u) :MyComponent() {
    override val title = "real tan distribution"

    val domain = Range(-7.5, 2.5)
    val range :Rect
    lateinit var scale :Rect

    private val ys = (-steps.toInt()..steps.toInt()).map { 0.0 }.toMutableList()

    init {
        val scale = (2*steps.toInt())/domain.length
        var y = random.nextGaussian()
        val N = 100_000*steps.toInt()
        repeat(N) {
            val i = ((y-domain.x0)*scale).roundToInt()
            if (i in ys.indices) ys[i]++
            y = iter(y)
        }
        val f = scale/N
        ys.indices.forEach { ys[it] *= f }
        val ym = ys.max()
        range = Rect.of(domain.x0, 0.0, domain.x1, ym)
    }

    fun iter(x :Double) = ln(abs(x))

    fun p(x :Double) :Double {
        val y = x+0.05
        val z2 = sqr(y)
        return exp(-z2)*0.42
    }

    override fun paint(g :Graphics) {
        scale = computeScaleIndep(width, height, range)
        g.drawAxes()
        var lastX = scale.px(domain.x0); var lastY = scale.py(ys.first())
//        var lastY1 = scale.py(p(domain.x0))
        (domain.iterator(2u*steps).toList() zip ys).forEach { (x, y) ->
            val px = scale.px(x);  val py = scale.py(y)
            g.color = Color.blue
            g.drawLine(lastX,lastY, px, py)
            lastX = px;  lastY = py
//            val y1 = p(x)
//            if (y1!=null) {
//                g.color = Color.gray
//                val py1 = scale.py(y1)
//                g.drawLine(lastX, lastY1, px, py1)
//                lastY1 = py1
//            }
        }
    }

    private fun Graphics.drawAxes() {
        color = Color.black
        val x0 = scale.px(0.0)
        drawLine(x0,0, x0, height)
        val dy = 10.0.pow(log10(max(abs(range.y0), abs(range.y1))).toInt()-1)
        for (y1 in (range.y0/dy).roundToInt()..(range.y1/dy).roundToInt()) {
            val y = y1*dy
            val py = scale.py(y)
            drawLine(x0-5,py, x0+5,py)
            drawString("%.2g".format(y), x0-35,py+4)
        }
        val y0 = scale.py(0.0)
        drawLine(0,y0, width,y0)
        val dx = 10.0.pow(log10(max(abs(range.x0), abs(range.x1()))).toInt())
        for (x1 in (range.x0/dx).roundToInt()..(range.x1()/dx).roundToInt()) {
            val x = x1*dx
            val px = scale.px(x)
            drawLine(px, y0-5,px,y0+5)
            drawString("%.2g".format(x), px-10,y0+20)
        }
    }
}

fun computeScaleIndep(width :Int, height :Int, range :Rect) :Rect {
    val dx = 0.9*width/range.dx;  val dy = 0.9*height/range.dy
    return Rect(range.x0 -(width/dx-range.dx)/2, range.y1 +(height/dy -range.dy)/2, dx, -dy)
}


data class Range(val x0 :Double, val x1 :Double) {
    fun iterator(numSteps :UInt = 1000u) = RangeIterator(x0, x1, (x1-x0)/numSteps.toInt())
    val length = x1-x0
}
