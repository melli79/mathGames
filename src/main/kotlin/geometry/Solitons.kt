package geometry

import chaos.MyComponent
import chaos.MyWindow
import common.math.epsilon
import common.math.geometry.Rect
import common.math.geometry.x1
import common.math.sqr
import common.step
import statistics.DiagramComponent.Companion.computeScale
import java.awt.Color
import java.awt.Graphics
import javax.swing.Timer
import kotlin.math.cbrt
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.roundToInt

class Solitons(val ln_tau :(Solitons.(Double)->Double) = Solitons::ln_tau3) : MyComponent() {
    override val title = "Solitons on the Computer"

    val ks = listOf(1.0, 1.5, 2.0)
    val xis = listOf(0.0, -5.0, -10.0)
    val aij = listOf(aij(ks[1], ks[0]), aij(ks[2], ks[0]), aij(ks[2], ks[1]))
    val tRange = -11.0..11.0
    var t = 0.0
    val dt = 0.05
    val dx = cbrt(epsilon)
    val range = Rect.of(-10.0, 0.0, 10.0, 8.0)
    private lateinit var scale :Rect
    private var timer :Timer? = null

    fun ln_tau2(x :Double) :Double {
        val eta0 = ks[0]*(x-sqr(ks[0])*t) +xis[0]
        val eta1 = ks[1]*(x-sqr(ks[1])*t) +xis[1]
        return ln(1.0
            + ks[0]*exp(2*eta0) +ks[1]*exp(2*eta1)
            +aij[0]*exp(2*eta0+2*eta1)
        )
    }


    fun ln_tau3(x :Double) :Double {
        val eta0 = ks[0]*(x-sqr(ks[0])*t) +xis[0]
        val eta1 = ks[1]*(x-sqr(ks[1])*t) +xis[1]
        val eta2 = ks[2]*(x-sqr(ks[2])*t) +xis[2]
        return ln(1.0
            + ks[0]*exp(2*eta0) +ks[1]*exp(2*eta1) +ks[2]*exp(2*eta2)
            +aij[0]*exp(2*eta0+2*eta1) +aij[1]*exp(2*eta0+2*eta2) +aij[2]*exp(2*eta1+2*eta2)
            +aij[0]*aij[1]*aij[2]*exp(2*eta0+2*eta1+2*eta2)
        )
    }

    fun u(x :Double) :Double {
        return (ln_tau(x+dx)-2*ln_tau(x)+ln_tau(x-dx))/sqr(dx)*2
    }

    fun aij(ki :Double, kj :Double) = sqr((ki-kj)/(ki+kj))

    fun update() {
        t += dt
        if (t !in tRange) t = tRange.start
        repaint()
    }

    override fun paint(g :Graphics) {
        if (timer==null) {
            timer = Timer(25) { update() }
            timer?.start()
        }
        scale = computeScale(width, height, range)
        g.drawAxes()
        g.color = Color.BLUE
        var lastX = scale.px(range.x0);  var lastY = scale.py(0.0)
        for (x in range.x0..range.x1() step 0.05) {
            val y = u(x)
            val px = scale.px(x);  val py = scale.py(y)
            g.drawLine(lastX, lastY, px, py)
            lastX = px;  lastY = py
        }
    }

    private fun Graphics.drawAxes() {
        drawString("t = %.2f s".format(t), 20,20)
        val px0 = scale.px(0.0)
        val py0 = scale.py(0.0)
        color = Color.GRAY
        drawLine(0, py0, width,py0)
        drawString("x", width-20, py0+20)
        val dx0 = 10.0.pow(round(log10(range.x1()))-1)
        for (x1 in (range.x0/dx0).roundToInt()..(range.x1()/dx0).roundToInt()) {
            val x = dx0*x1
            val px = scale.px(x)
            drawLine(px, py0-5, px, py0+5)
            drawString("%.1f".format(x), px-10, py0+10)
        }
        drawLine(px0, 0, px0, height)
        val dy0 = 10.0.pow(round(log10(range.x1()))-1)
        for (y1 in (range.y0/dy0).roundToInt()..(range.y1/dy0).roundToInt()) if (y1!=0) {
            val y = dy0*y1
            val py = scale.py(y)
            drawLine(px0-5, py, px0+5, py)
            drawString("%.1f".format(y), px0-25, py+5)
        }
    }
}

fun main() {
    val w = MyWindow(Solitons())
    w.isVisible = true
}
