package chaos

import common.math.Complex
import common.math.geometry.Rect
import java.awt.Color
import java.awt.Graphics

fun cb(z :Complex) = z*z*z
fun sqr(z :Complex) = z*z
fun bsqr(z :Complex) = sqr(sqr(z))
fun pent(z :Complex) = z*bsqr(z)

class NewtonFractal :MyComponent() {
    companion object {
        val brown = Color(128, 64, 0)
        val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE, brown, Color.CYAN)
    }
    override val title = "Newton Fractal -- z^5+1 ≈ 0"
    val range = Rect.of(-5.0, -4.0, 5.0, 4.0)
    private lateinit var scale :Rect
    var numIt = 64u

    fun iter3(z0 :Complex) :UShort {
        var z = z0
        for (n in 1u..numIt) {
            z -= (cb(z) + Complex.ONE)/(sqr(z)*3.0)
        }
        return when {
            z.re<-0.25 -> 0u
            z.im<0 -> 1u
            else -> 2u
        }
    }

    fun iter5(z0 :Complex) :UShort {
        var z = z0
        for (n in 1u..numIt) {
            z -= (pent(z) + Complex.ONE)/(bsqr(z)*5.0)
        }
        return when {
            z.re<-0.5 -> 0u
            z.re<0.25 -> if (z.im<0) 1u  else 2u
            else -> if (z.im<0) 3u  else 4u
        }
    }

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        var y = scale.y(0)
        for (py in 0..<height) {
            var x = scale.x(0)
            for (px in 0..<width) {
                val c = iter5(Complex(x, y))
                g.color = colors[c.toInt()]
                g.fillRect(px, py, 1,1)
                x += 1/scale.dx
            }
            y += 1/scale.dy
        }
    }
}
