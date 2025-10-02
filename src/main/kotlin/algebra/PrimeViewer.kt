package algebra

import chaos.MyComponent
import chaos.MyWindow
import chaos.computeScale
import common.math.geometry.Rect
import common.math.geometry.x1
import java.awt.Color
import java.awt.Graphics
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt

class PrimeViewer(private var limit :UInt) :MyComponent() {
    override val title = "Eistenstein Prime Viewer"

    lateinit var primes :List<Eisenstein>
    lateinit var range :Rect
    private lateinit var scale :Rect

    init {
        generatePrimes()
    }

    private fun generatePrimes() {
        val primes = mutableListOf<Eisenstein>()
        for (a in 0..limit.toInt()) for (b in 0..limit.toInt()) {
            val p = Eisenstein(a, b)
            if (p.isPrime() == true)
                primes.addAll(Eisenstein.units.map { p*it })
        }
        val xm = primes.minOf { it.re }
        val xM = primes.maxOf { it.re }
        val ym = primes.minOf { it.im }
        val yM = primes.maxOf { it.im }
        this.primes = primes
        range = Rect.of(xm, ym, xM, yM)
    }

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        g.drawAxes()
        g.color = Color.red.darker()
        for (u in Eisenstein.units) {
            g.fillRect(scale.px(u.re)-1, scale.py(u.im)-1, 3, 3)
        }
        g.color = Color.green.darker()
        for (p in primes)
            g.fillRect(scale.px(p.re)-1, scale.py(p.im)-1, 3, 3)
    }

    override fun scaleUp() {
        limit += limit/2u
        if (limit>100u) limit = 100u
        generatePrimes()
        repaint()
    }

    override fun scaleDown() {
        limit -= limit/4u
        if (limit<5u) limit = 5u
        generatePrimes()
        repaint()
    }

    private fun Graphics.drawAxes() {
        color = Color.gray
        val px0 = scale.px(0.0)
        drawLine(px0, 0, px0, height-1)
        val dy = 10.0.pow(((log10(max(abs(range.y0), abs(range.y1)))*3).roundToInt()-1)/3.0).roundToInt().toDouble()
        for (y1 in (range.y0/dy).roundToInt()..(range.y1/dy).roundToInt()) if (y1!=0) {
            val y = y1*dy
            val py = scale.py(y)
            drawLine(px0-5,py, px0+5,py)
            drawString("%d".format(y.roundToInt()), px0-25,py+4)
        }
        val py0 = scale.py(0.0)
        drawLine(0,py0, width-1, py0)
        val dx = 10.0.pow(((log10(max(abs(range.x0), abs(range.x1())))*3).toInt()-1)/3.0).roundToInt().toDouble()
        for (x1 in (range.x0/dx).roundToInt()..(range.x1()/dx).roundToInt()) {
            val x = x1*dx
            val px = scale.px(x)
            drawLine(px,py0-5, px,py0+5)
            drawString("%d".format(x.roundToInt()), px-10,py0+24)
        }
    }
}

fun main() {
    val w = MyWindow(PrimeViewer(20u))
    w.isVisible = true
}
