package geometry

import chaos.MyComponent
import chaos.MyWindow
import chaos.computeScale
import common.math.Complex
import common.math.geometry.Rect
import java.awt.Color
import java.awt.Graphics
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.pow

/**
 * Bioelectrical Impedance Analysis
 * application: estimating body fat and hydration
 */
class Bia(mode :Mode = Mode.MuscleImpedance) : MyComponent() {

    companion object {
        val tau :Double = 1/(2*Math.PI*50.0E+3)
        val ln10 = Math.log(10.0)
        val alpha = 0.15
    }

    sealed class Mode(val title :String) {
        abstract fun computeRange() :Rect
        fun drawAxes(g :Graphics, scale :Rect, width :Int, height :Int) {
            g.color = Color.black
            val x0 = scale.px(0.0)
            g.drawLine(x0, 0, x0, height)
            val y0 = scale.py(0.0)
            g.drawLine(0, y0, width, y0)
            scaleAxes(g, scale, width, height)
        }
        abstract fun scaleAxes(g: Graphics, scale :Rect, width :Int, height :Int)
        abstract fun drawCurve(g: Graphics, scale :Rect)

        object MuscleImpedance :Mode("Muscle Impedance") {
            val r0 = 267.5
            val rInf = 200.0

            override fun computeRange() = Rect.of(x0= 0.0, x1= max(r0, rInf), y0= 0.0, y1= abs(r0-rInf)/2.0)

            override fun scaleAxes(g :Graphics, scale :Rect, width :Int, height: Int) {
                val x1 = scale.px(r0)
                val y0= scale.py(0.0)
                g.drawLine(x1, y0-5, x1, y0+5)
                g.drawString("R_0", x1-10, y0+16)
                val rMean = (r0 + rInf)/2
                val x2 = scale.px(rMean)
                g.drawLine(x2, y0-5, x2, y0+5)
                g.drawString("(R_0+R_Inf)/2", x2-50, y0+16)
                val xInf = scale.px(rInf)
                g.drawLine(xInf, y0-5, xInf, y0+5)
                g.drawString("R_Inf", xInf-15, y0+16)
                val x0= scale.px(0.0)
                val dr = abs(r0 - rInf)/2.0
                val y1 = scale.py(dr)
                g.drawLine(x0-5, y1, x0+5, y1)
                g.drawString("∆R/2", x0-24, y1+4)
                val y2 = scale.py(-dr)
                g.drawLine(x0-5, y2, x0+5, y2)
                g.drawString("-∆R/2", x0-24, y2+4)
            }

            override fun drawCurve(g: Graphics, scale :Rect) {
                g.color= Color.red.darker()
                var lastX = scale.px(r0);  var lastY = scale.py(0.0)
                for (f in -50..50) {
                    val omega = 1E+3*exp(f*0.25)
                    val z = Complex(rInf, 0.0) + Complex(r0 - rInf, 0.0)/Complex(1.0, (tau*omega).pow(1.0-alpha))
                    println(z)
                    val px = scale.px(z.re); val py = scale.py(z.im)
                    g.drawLine(lastX, lastY, px, py)
                    lastX = px; lastY = py
                }
            }
        }

        open class BodyImpedance(prefix :String = "") :Mode(prefix+"Body Impedance") {
            val rA = 250.0
            val rT = 17.5
            open val rTinf = rT/2
            val rL = 250.0
            val r0 = rA/2 + rT + rL/2
            override fun computeRange() = Rect.of(x0= 0.0, x1= r0, y0= -r0/2, y1= r0/2)

            override fun scaleAxes(g :Graphics, scale :Rect, width :Int, height: Int) {
                val rInf = r0/2
                val x1 = scale.px(r0)
                val y0= scale.py(0.0)
                g.drawLine(x1, y0-5, x1, y0+5)
                g.drawString("R_0", x1-10, y0+16)
                val rMean = (r0 + rInf)/2
                val x2 = scale.px(rMean)
                g.drawLine(x2, y0-5, x2, y0+5)
                g.drawString("(R_0+R_Inf)/2", x2-50, y0+16)
                val xInf = scale.px(rInf)
                g.drawLine(xInf, y0-5, xInf, y0+5)
                g.drawString("R_Inf", xInf-15, y0+16)
                val x0= scale.px(0.0)
                val dr = abs(r0 - rInf)/2.0
                val y1 = scale.py(dr)
                g.drawLine(x0-5, y1, x0+5, y1)
                g.drawString("∆R/2", x0-24, y1+4)
                val y2 = scale.py(-dr)
                g.drawLine(x0-5, y2, x0+5, y2)
                g.drawString("-∆R/2", x0-24, y2+4)
            }

            override fun drawCurve(g: Graphics, scale :Rect) {
                g.color= Color.red.darker()
                var lastX = scale.px(r0);  var lastY = scale.py(0.0)
                for (f in -20..50) {
                    val omega = 10E+3*Math.PI*exp(f*ln10/10)
                    val zA = Complex(rA/2, 0.0) + Complex(rA/2, 0.0)/Complex(1.0, (tau*omega).pow(1.0-alpha))
                    val zT = Complex(rTinf, 0.0) + Complex(rT-rTinf, 0.0)/Complex(1.0, (tau*omega).pow(1.0-alpha))
                    val zL = Complex(rL/2, 0.0) + Complex(rL/2, 0.0)/Complex(1.0, (tau*omega).pow(1.0-alpha))
                    val z = zA*0.5 + zT + zL*0.5
                    println(z)
                    val px = scale.px(z.re); val py = scale.py(z.im)
                    g.drawLine(lastX, lastY, px, py)
                    if (f==0 || f==10 || f==17) g.fillOval(px-2, py-2, 5,5)
                    lastX = px;  lastY = py
                }
            }
        }

        object FatBodyImpedance : BodyImpedance("fat ") {
            override val rTinf = rT
        }
    }

    var mode = mode
        set(value) {
            field = value
            range = mode.computeRange()
        }
    override val title :String
        get() = "BIA -- ${mode.title}"

    var range = mode.computeRange()
        private set
    private lateinit var scale :Rect

    override fun paint(g :Graphics) {
        scale = computeScale(width, height, range)
        mode.drawAxes(g, scale, width, height)
        mode.drawCurve(g, scale)
    }
}

fun main() {
    val w = MyWindow(Bia(Bia.Mode.FatBodyImpedance) as MyComponent)
    w.isVisible = true
}
