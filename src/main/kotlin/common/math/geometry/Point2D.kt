package common.math.geometry

data class Point2D(val x :Double, val y :Double) {
    companion object {
        val ORIGIN = Point2D(0.0, 0.0)
    }

    operator fun minus(o :Point2D) = Vector2D(x-o.x, y-o.y)

    fun translate(v :Vector2D) = Point2D(x+v.x, y+v.y)
    fun affine(p :Point2D, t :Double) = translate(p.minus(this)*t)
}

fun Point2D.scale(o :Point2D, lambda :Double) = o.translate((this-o)*lambda)