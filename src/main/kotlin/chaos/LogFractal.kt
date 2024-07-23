package chaos

import common.math.*
import common.math.geometry.Rect
import common.math.statistics.cumsum
import common.math.geometry.x1
import java.awt.Color
import java.awt.Graphics
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.awt.image.BufferedImage
import java.util.Random
import kotlin.math.*

val range0 = Rect.of(-0.8, -1.8, 7.4, 1.8)

class LogFractal(val colorPattern :ColorPattern = ColorPattern.PATCHED) :MyComponent(), MouseMotionListener, ComponentListener {
    override val title = "Log Frractal"

    enum class ColorPattern {
        MONOCHROME, BACKGROUND, INDEX, PATCHED, OSC
    }

    companion object {
        val lightBlue = Color(96,128,255)
        val lightLightBlue = Color(192,224,255)
        val darkGreen = Color(0,128,0)
        val lightGreen = Color(128,255,128)
        val darkCyan = Color(0,128,128)
        val lightCyan = Color(128, 255,255)
        val darkRed = Color(192,0,0)
        val orange = Color(255,192,128)
        val lightPurple = Color(224,128,255)
        val lightGray = Color(192, 192, 192)
        val purple = Color(96,0,128)
        val brown = Color(128,96,0)
        val lightColors = arrayOf(lightBlue, lightGreen, lightCyan, orange, lightPurple, Color.yellow)
        val darkColors = arrayOf(Color.black, Color.blue, darkGreen, darkCyan, darkRed, purple, brown)

        fun getLightColor(c :Int) = if (c<0) lightColors[(lightColors.size -((-c)%lightColors.size)) % lightColors.size]
        else if (c>0) lightColors[c % lightColors.size]  else lightLightBlue

        fun getDarkColor(c :Int) = if (c<0) darkColors[(darkColors.size -((-c)%darkColors.size)) % darkColors.size]
        else darkColors[c % darkColors.size]
    }

    private var range = range0
    private lateinit var scale :Rect

    private var x0 = 0.0
    private var y0 = 0.0
    private var r = 0.0
    private var phi = 0.0

//    init {
//        addMouseMotionListener(this)
//    }

    fun computeColor(x0 :Double, y0 :Double, depth :UShort = 5u) :Int {
        var z = Complex(x0, y0)
        for (n in 1..depth.toInt()) {
            z = log(z)
        }
        return when (depth.toInt()) {
            1, 3 -> (atan2(z.im, -z.re)/PI).roundToInt()
            2 -> (atan2(z.im, z.re)*4/PI).roundToInt()
            4 -> (if (z.im>0.0) 1  else -1)*(if (z.abs2()>2.0) 2  else 1)
            else -> (atan2(z.im, z.re)*depth.toInt()/PI).roundToInt()
        }
    }

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        if (colorPattern == ColorPattern.BACKGROUND)
            g.colorize()
        else if (colorPattern == ColorPattern.OSC)
            g.paintOsc()
        else {
            g.color = Color.white
            g.fillRect(0, 0, width, height)
            if (colorPattern == ColorPattern.PATCHED)
                g.colPatches()
        }
        g.paintFractal()
//        g.color = Color.black
//        g.drawString("r=%.2f, \\phi=%.1f (%+.3f, %+.3f)".format(r, phi, x0, y0), 5, 20)
    }

    private fun Graphics.colorize() {
        for (py in 0..height / 2) {
            val y = scale.y(py)
            for (px in 0..< width) {
                val x = scale.x(px)
                color = getLightColor(computeColor(x, y))
                fillRect(px, py, 1, 1)
                color = getLightColor(computeColor(x, -y))
                fillRect(px, height - py, 1, 1)
            }
        }
    }

    private fun Graphics.paintOsc() {
        val n = 3
        var f = 0.0
        val fs = (0..n).map { val fn = f; f = exp(f); fn }
        val epsilon_n = 1.25
        val xm = ln(epsilon_n) - n*(n+1)/2
        val epsilon_m = exp(xm)
        val px0 = scale.px(xm)
        drawLine(px0,0, px0,height)
        val y0 = -PI;  val x0 = xm+3
        val y1 = PI;  val x1 = range.x1()+6
        for (k in 0..30) {
            color = getLightColor(k)
            val dx = min(0.1, 0.003*exp(0.5*k))
            for (x in linspace(x0, x1, dx)) {
                val po = Complex(x, y0)
                val zi = log(po*(epsilon_m/sqrt(po.abs2())) + Complex.I*(2*PI*k))
                val xl = scale.px(zi.re);  val yl = scale.py(zi.im)
                val zo = log(po + Complex.I*(2*PI*k))
                val xr = scale.px(zo.re);  val yr = scale.py(zo.im)
                drawLine(xl,yl, xr,yr)
                if (k>0) {
                    val zi2 = log(po*(epsilon_m/sqrt(po.abs2())) + Complex.I*(-2*PI*k))
                    val xl2 = scale.px(zi2.re);  val yl2 = scale.py(zi2.im)
                    val zo2 = log(po + Complex.I*(-2*PI*k))
                    val xr2 = scale.px(zo2.re);  val yr2 = scale.py(zo2.im)
                    drawLine(xl2, yl2, xr2, yr2)
                }
            }
            for (y in linspace(y0, y1, dx)) {
                val po = Complex(x0, y)
                val zi = log(po*(epsilon_m/sqrt(po.abs2())) + Complex.I*(2*PI*k))
                val xl = scale.px(zi.re);  val yl = scale.py(zi.im)
                val zo = log(po + Complex.I*(2*PI*k))
                val xr = scale.px(zo.re);  val yr = scale.py(zo.im)
                drawLine(xl,yl, xr,yr)
                if (k>0) {
                    val zi2 = log(po*(epsilon_m/sqrt(po.abs2())) + Complex.I*(-2*PI*k))
                    val xl2 = scale.px(zi2.re);  val yl2 = scale.py(zi2.im)
                    val zo2 = log(po + Complex.I*(-2*PI*k))
                    val xr2 = scale.px(zo2.re);  val yr2 = scale.py(zo2.im)
                    drawLine(xl2, yl2, xr2, yr2)
                }
            }
            for (x in linspace(x0, x1, dx)) {
                val po = Complex(x, y1)
                val zi = log(po*(epsilon_m/sqrt(po.abs2())) + Complex.I*(2*PI*k))
                val xl = scale.px(zi.re);  val yl = scale.py(zi.im)
                val zo = log(po + Complex.I*(2*PI*k))
                val xr = scale.px(zo.re);  val yr = scale.py(zo.im)
                drawLine(xl,yl, xr,yr)
                if (k>0) {
                    val zi2 = log(po*(epsilon_m/sqrt(po.abs2())) + Complex.I*(-2*PI*k))
                    val xl2 = scale.px(zi2.re);  val yl2 = scale.py(zi2.im)
                    val zo2 = log(po + Complex.I*(-2*PI*k))
                    val xr2 = scale.px(zo2.re);  val yr2 = scale.py(zo2.im)
                    drawLine(xl2, yl2, xr2, yr2)
                }
            }
            for (y in linspace(y0, y1, dx)) {
                val po = Complex(x1, y)
                val zi = log(po*(epsilon_m/sqrt(po.abs2())) + Complex.I*(2*PI*k))
                val xl = scale.px(zi.re);  val yl = scale.py(zi.im)
                val zo = log(po + Complex.I*(2*PI*k))
                val xr = scale.px(zo.re);  val yr = scale.py(zo.im)
                drawLine(xl,yl, xr,yr)
                if (k>0) {
                    val zi2 = log(po*(epsilon_m/sqrt(po.abs2())) + Complex.I*(-2*PI*k))
                    val xl2 = scale.px(zi2.re);  val yl2 = scale.py(zi2.im)
                    val zo2 = log(po + Complex.I*(-2*PI*k))
                    val xr2 = scale.px(zo2.re);  val yr2 = scale.py(zo2.im)
                    drawLine(xl2, yl2, xr2, yr2)
                }
            }
        }
        color = Color.gray
        for (k in 0..20) {
            var epsilon = epsilon_n
            for (fn in fs.drop(1).reversed()) {
                val zn = log(Complex(fn, 2*PI*k))
                val px = scale.px(zn.re);  val py = scale.py(zn.im)
                val dx = (scale.dx*epsilon/sqrt(sqr(fn)+sqr(2*PI*k))).roundToInt()
                fillOval(px - dx, py - dx, 2*dx + 1, 2*dx + 1)
                val py1 = scale.py(-zn.im)
                fillOval(px - dx, py1 - dx, 2*dx + 1, 2*dx + 1)
                epsilon /= fn
            }
        }
    }

    private fun Graphics.colPatches() {
        var z = Complex(0.5, 0.5)
        for (n in 0..10) {
            z = log(z + Complex.I*(2*PI*LogWeights.nextIndex(z, random)))
        }
        val lastPoints = (0..30).map { Pair(0,0) }.toMutableList()
        for (n in 1..(width*height / 9)) {
            val index = LogWeights.nextIndex(z, random)
            z = log(z + Complex.I * (2*PI*index))
            if (abs(index) in 1L..15L) {
                color = getLightColor(index.toInt())
                val lastP = lastPoints[15+index.toInt()]
                val px = scale.px(z.re);  val py = scale.py(z.im)
                if (lastP!=Pair(0,0))
                    drawLine(lastP.first, lastP.second, px, py)
                lastPoints[15+index.toInt()] = Pair(px, py)
            }
        }
    }

    private fun Graphics.paintFractal() {
        var z = Complex(0.5, 0.5)
        for (n in 0..10) {
            z = log(z + Complex.I*(2*PI*LogWeights.nextIndex(z, random)))
        }
        color = Color.black
        for (n in 1..(width*height / 9)) {
            val index = LogWeights.nextIndex(z, random)
            z = log(z + Complex.I*(2*PI*index))
            if (colorPattern==ColorPattern.INDEX && index!=0L)
                color = getDarkColor(index.toInt())
            fillRect(scale.px(z.re), scale.py(z.im), 1, 1)
            fillRect(scale.px(z.re), scale.py(-z.im), 1, 1)
        }
    }

    override fun mouseDragged(event :MouseEvent) {
        mouseMoved(event)
    }

    override fun mouseMoved(event :MouseEvent) {
        x0 = scale.x(event.x);   y0 = scale.y(event.y)
        r = sqrt(sqr(x0)+sqr(y0))
        phi = atan2(y0, x0)/PI*180
        repaint()
    }

    override fun componentResized(event :ComponentEvent) {
        println("resized")
        repaint()
    }

    override fun componentMoved(ignore :ComponentEvent) {
    }

    override fun componentShown(event :ComponentEvent) {
        println("became visible")
        repaint()
    }

    override fun componentHidden(ignore :ComponentEvent) {
    }
}

object LogWeights {
    const val cutoff = 400
    val xs = listOf(0.0, 0.05, 0.1, 0.31, 1.0, 3.1, 10.0)
    val ys = listOf(0.0, 0.25*PI, 0.5*PI, 0.75*PI, PI, 1.25*PI, 1.5*PI, 1.75*PI)
    private val ps0 = (0..< cutoff).map { 1/(0.3+2*PI*it).pow(1.2) }
    val psy = listOf(ps0.cumsum()) + ys.drop(1).map { y -> (0..< cutoff).map { (y+PI*2*it).pow(-1.2) }.cumsum() }
    val ps = xs.drop(1).map { x ->
        (0..< cutoff).map { 1 / (Complex(x, 2*PI*it)).abs2().pow(0.6) }
    }
    val levels = listOf(ps0.cumsum()) + ps.map { it.cumsum() }

    fun nextIndex(z :Complex, random :Random) :Long = if (random.flipCoin()) -random.nextLevel(z) else random.nextLevel(z)

    fun Random.nextLevel(z :Complex) :Long {
        val px = xs.findNN(abs(z.re))
        val levels: List<Double> = if (px>0) levels[px]
        else {
            val py = ys.findNN(abs(z.im))
            psy[py]
        }
//        val levels = (0..cutoff).map { 1 / ((z + Complex.I*(2*PI*it)).abs2()).pow(0.675) }.cumsum()
        val scale = levels[cutoff -1] * 1.05
        val r = nextDouble() * scale * 1.05
        val result = levels.indexOfLast { it <= r }
        return if (result < cutoff-1) result.toLong()  else cutoff + abs(nextGaussian().toLong())
    }
}

fun List<Double>.findNN(x: Double): Int {
    var mi=0; var ma=size-1
    while (mi+1<ma) {
        val i = (mi+ma)/2
        val cmp = get(i).compareTo(x)
        when {
            cmp < 0 -> mi = i
            cmp > 0 -> ma = i
            else -> return i
        }
    }
    return if (x-get(mi)<get(ma)-x) mi  else ma
}

fun Random.flipCoin(bias :Double =0.5) = nextDouble() < bias
fun Random.nextSgn() = if (nextBoolean()) 1  else -1
fun ohex(x :Double) = sqr(sqr(x))*abs(x)*x
fun epent(x :Double) = sqr(sqr(x))*abs(x)
fun obsqr(x :Double) = x*abs(cb(x))

fun computeScale(width :Int, height :Int, range :Rect) :Rect {
    val dx = 0.9*min(width/range.dx, height/range.dy)
    return Rect(range.x0 -(width/dx-range.dx)/2, range.y1 +(height/dx -range.dy)/2, dx, -dx)
}

class Estimator {

    private lateinit var scale :Rect

    val samples = mutableListOf<Pair<Double, Double>>()

    fun computeSamples() :List<Pair<Double, Double>> {
        if (samples.size>5)  return samples
        samples.clear()
        var width = 10
        while (width<15_000) {
            val height = width/2
            val numPixels = paint(width, height)
            samples.add(Pair(1/scale.dx, numPixels.toDouble()))
            println("${1/scale.dx}: $numPixels")
            width += width/2
        }
        return samples
    }

    fun paint(width :Int, height :Int) :Long {
        scale = computeScale(width, height, range0)
        val buffer = BufferedImage(width, height, BufferedImage.BITMASK)
        var numPixels = 0L
        var z = Complex(0.5,0.5)
        for(n in 0..10) {
            z = log(z+ Complex.I*(2*PI*LogWeights.nextIndex(z, random)))
        }
        for (n in 1..(width*height/10)) {
            val px = scale.px(z.re)
            val py = scale.py(z.im)
            if (px in 0..< width && py in 0..< height && buffer.getRGB(px, py)==0) {
                buffer.setRGB(px, py, 1)
                numPixels++
            }
            val py1 = scale.py(-z.im)
            if (px in 0..< width && py1 in 0..< height && buffer.getRGB(px, py1)==0) {
                buffer.setRGB(px, py1, 1)
                numPixels++
            }
            z = log(z+ Complex.I*(2*PI*LogWeights.nextIndex(z, random)))
        }
        return numPixels
    }

}

class Liminator :MyComponent() {
    override val title = "Limits of the iterated logarithm"

    val range = Rect.of(-2.0, -2.0, 6.0, 2.0)
    private lateinit var scale :Rect

    fun iterate(x0 :Double, y0 :Double, depth :UShort = 4u) :Complex {
        var z = Complex(x0, y0)
        for (n in 1..depth.toInt()) {
            z = log(z)
        }
        return z
    }

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        g.drawAxes()
        g.color = Color.black
        val dy = 0.05
        for (y in RangeIterator(0.0, range.y1, dy)) {
            for (x in RangeIterator(range.x0, range.x1(), range.dx)) {
                val z1 = iterate(x, y)
                g.fillRect(scale.px(z1.re)-1, scale.py(z1.im)-1, 3, 3)
                val z2 = iterate(x, -y)
                g.fillRect(scale.px(z2.re)-1, scale.py(z2.im)-1, 3, 3)
                println("$z1, $z2")
            }
        }
    }

    private fun Graphics.drawAxes() {
        color = Color.gray
        val x0 = scale.px(0.0)
        drawLine(x0, 0, x0, height)
        for (y in RangeIterator(-2.0, 2.0, 0.5)) {
            val py = scale.py(y)
            drawLine(x0 -5, py, x0 +5, py)
        }
        val y0 = scale.py(0.0)
        drawLine(0, y0, width, y0)
        for (x in RangeIterator(-2.0, 6.0, 1.0)) {
            val px = scale.px(x)
            drawLine(px, y0 -5, px, y0 +5)
        }
    }
}

class LogOsc :MyComponent() {
    override val title = "Patches of the Open Set Condition for the Log Fractal"

    val outer = Rect.of(-4.0, -3.0, 5.2, 3.0)
    val innerR = 0.02

    private lateinit var scale :Rect

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, outer)
        for (n in 0..40) {
            g.drawPatch(n)
            if (n>0) g.drawPatch(-n)
        }
        g.color = Color.black
        g.drawCircle(innerR, true)
        g.fillOval(scale.px(1.0)-1, scale.py(0.0)-1, 3,3)
        g.fillOval(scale.px(E)-1, scale.py(0.0)-1, 3,3)
        g.fillOval(scale.px(exp(E))-1, scale.py(0.0)-1, 3,3)
    }

    private fun Graphics.drawCircle(r: Double, full :Boolean =false) {
        val xl = scale.px(-r);  val yu = scale.py(r)
        val iw = (scale.dx * r * 2).roundToInt()
        if (full) fillOval(xl, yu, iw, iw)
        else drawOval(xl, yu, iw, iw)
    }

    private fun Graphics.drawPatch(n: Int) {
        color = LogFractal.getLightColor(n)
        for (opx in 0..<width) {
            val ox = scale.x(opx)
            val op1 = log(Complex(ox, outer.y1 + 2*PI*n))
            drawSegment(op1, atan2(outer.y1, ox), n)
            val op0 = log(Complex(ox, outer.y0 + 2*PI*n))
            drawSegment(op0, atan2(outer.y0, ox), n)
        }
        for (opy in 1..<height) {
            val oy = scale.y(opy)
            val op = log(Complex(outer.x0, oy + 2*PI*n))
            drawSegment(op, atan2(oy, outer.x0), n)
            val op1 = log(Complex(outer.x1(), oy + 2*PI*n))
            drawSegment(op1, atan2(oy, outer.x1()), n)
        }
    }

    private fun Graphics.drawSegment(op: Complex, phi: Double, n :Int) {
        val opx = scale.px(op.re);  val opy = scale.py(op.im)
        val ip = log(Complex(innerR*cos(phi), innerR*sin(phi) +2*PI*n))
        val ipx = scale.px(ip.re);  val ipy = scale.py(ip.im)
        drawLine(opx, opy, ipx, ipy)
    }
}

fun main() {
    val z = Complex.ofPolar(1.15, 160*PI/180)
    for (s in linspace(1.4, 1.8, 0.05))
        println("\\zeta(%.2f, z) = %.4f".format(s, zeta(s, z)))
//    val estimator = Estimator()
//    val loglog = estimator.computeSamples().map { (r, n) -> Pair(-ln(r), ln(n)) }
//    val (m,b, dm, db) = linReg(loglog)
//    println("box dimension: %.2f±%.2f with offset %.2f±%.2f".format(m, dm, b, db))
}

fun zeta(s :Double, z :Complex, n :Long = 20_000_000L) = z.abs2().pow(-s/2) +2*
        (1L..n).sumOf { (z+Complex(0.0,2*PI*it)).abs2().pow(-s/2) }
