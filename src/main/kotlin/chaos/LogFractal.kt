package chaos

import common.Quadruple
import common.math.*
import common.math.geometry.Rect
import common.math.statistics.cumsum
import probability.x1
import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.util.Random
import kotlin.math.*

val range = Rect.of(-1.6, -1.8, 7.4, 1.8)

class LogFractal(val colorPattern :ColorPattern = ColorPattern.MONOCHROME) :MyComponent() {
    override val title = "Log Frractal"

    enum class ColorPattern {
        MONOCHROME, BACKGROUND, INDEX
    }

    companion object {
        val lightBlue = Color(96,128,255)
        val darkGreen = Color(0,128,0)
        val lightGreen = Color(128,255,128)
        val darkCyan = Color(0,128,128)
        val lightCyan = Color(128, 255,255)
        val darkRed = Color(192,0,0)
        val orange = Color(255,192,128)
        val lightPurple = Color(224,128,255)
        val purple = Color(96,0,128)
        val brown = Color(128,96,0)
        val lightColors = arrayOf(Color.lightGray, lightBlue, lightGreen, orange, lightCyan, lightPurple, Color.yellow)
        val darkColors = arrayOf(Color.black, Color.blue, darkGreen, darkCyan, darkRed, purple, brown)
    }

    private lateinit var scale :Rect

    fun getLightColor(c :Int) = if (c<0) lightColors[(lightColors.size -((-c)%lightColors.size)) % lightColors.size]
        else lightColors[c % lightColors.size]

    fun getDarkColor(c :Int) = if (c<0) darkColors[(darkColors.size -((-c)%darkColors.size)) % darkColors.size]
        else darkColors[c % darkColors.size]

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
        if (colorPattern==ColorPattern.BACKGROUND)
            g.colorize()
        g.paintFractal()
    }

    private fun Graphics.colorize() {
        for (py in 0..height / 2) {
            val y = scale.y(py)
            for (px in 0..<width) {
                val x = scale.x(px)
                color = getLightColor(computeColor(x, y))
                fillRect(px, py, 1, 1)
                color = getLightColor(computeColor(x, -y))
                fillRect(px, height - py, 1, 1)
            }
        }
    }

    private fun Graphics.paintFractal() {
        var z = Complex(0.5, 0.5)
        for (n in 0..10) {
            z = log(z + Complex.I*(2*PI*random.nextIndex(z)))
        }
        color = Color.black
        for (n in 1..(width*height / 9)) {
            val index = random.nextIndex(z)
            if (colorPattern==ColorPattern.INDEX && index!=0L)
                color = getDarkColor(index.toInt())
            z = log(z + Complex.I*(2*PI*index))
            fillRect(scale.px(z.re), scale.py(z.im), 1, 1)
            fillRect(scale.px(z.re), scale.py(-z.im), 1, 1)
        }
    }
}

fun Random.nextIndex(z :Complex) :Long = if (this.flipCoin()) -nextLevel(z)  else nextLevel(z)

fun Random.nextLevel(z :Complex) :Long {
    val levels = (0..300).map { 1/((z + Complex.I*(2*PI*it)).abs2()).pow(0.675) }.cumsum()
    val r = nextDouble()*levels.last()*1.1
    val result = levels.indexOfLast { it <= r }
    return if (result<levels.indices.last) result.toLong()  else levels.size+abs(nextGaussian().toLong())
}

//fun Random.nextLevel() = if (this.flipCoin(0.65)) 0  else nextSgn()*(1L+epent(2.3*nextGaussian()).toLong())

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
        scale = computeScale(width, height, range)
        val buffer = BufferedImage(width, height, BufferedImage.BITMASK)
        var numPixels = 0L
        var z = Complex(0.5,0.5)
        for(n in 0..10) {
            z = log(z+ Complex.I*(2*PI*random.nextIndex(z)))
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
            z = log(z+ Complex.I*(2*PI*(random.nextIndex(z))))
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
        val dx = 0.05;  val dy = 0.05
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

fun main() {
    val liminator = Liminator()
//    val estimator = Estimator()
//    val loglog = estimator.computeSamples().map { (r, n) -> Pair(-ln(r), ln(n)) }
//    val (m,b, dm, db) = linReg(loglog)
//    println("box dimension: %.2f±%.2f with offset %.2f±%.2f".format(m, dm, b, db))
}

fun linReg(xy :List<Pair<Double, Double>>, ws :(Int)->Double = {1.0}) :Quadruple<Double, Double, Double, Double> {
    val w = xy.indices.sumOf { ws(it) }
    val x = xy.mapIndexed { i, (x, _) -> x*ws(i) }.sum()
    val x2 = xy.mapIndexed { i, (x, _) -> x*x*ws(i) }.sum()
    val y = xy.mapIndexed { i, (_, y) -> y*ws(i) }.sum()
    val yx = xy.mapIndexed { i, (x, y) -> x*y*ws(i) }.sum()
    val y2 = xy.mapIndexed { i, (_, y) -> y*y*ws(i) }.sum()

    val dx = x2*w - x*x;  assert(dx > 0.0)
    val m = (yx*w-x*y) / dx
    val b = (y-m*x)/w
    val dy = y2*w + sqr(b*w) + m*m*x2*w -2*y*m*x -2*y*b*w +2*m*x*b
    val sigma = sqrt(dy /(w*w*(w-2)))
    return Quadruple(m,b, sigma*w*x/(dx*sqrt(2.0)), sigma)
}
