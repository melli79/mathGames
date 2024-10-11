package common.math

import common.Quadruple
import kotlin.math.sqrt

fun linReg(xy :List<Pair<Double, Double>>, ws :(Int)->Double = {1.0}) : Quadruple<Double, Double, Double, Double> {
    val w = xy.indices.sumOf { ws(it) }
    val x = xy.mapIndexed { i, (x, _) -> x*ws(i) }.sum()
    val x2 = xy.mapIndexed { i, (x, _) -> x*x*ws(i) }.sum()
    val y = xy.mapIndexed { i, (_, y) -> y*ws(i) }.sum()
    val yx = xy.mapIndexed { i, (x, y) -> x*y*ws(i) }.sum()
    val y2 = xy.mapIndexed { i, (_, y) -> y*y*ws(i) }.sum()

    val dx = x2*w - x*x;  assert(dx > 0.0)
    val m = (yx*w-x*y) / dx
    val b = (y-m*x)/w
    val dy = y2*w + sqr(b * w) + m*m*x2*w -2*y*m*x -2*y*b*w +2*m*x*b
    val sigma = sqrt(dy / (w * w * (w - 2)))
    return Quadruple(m, b, sigma * w * x / (dx * sqrt(2.0)), sigma)
}