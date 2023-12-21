package common.math.geometry

fun List<Point2D>.bezier(t :Double) :Point2D {
    if (size==1)
        return first()
    var qs = windowed(2) { ps -> ps[0].affine(ps[1], t) }
    while (qs.size>1)
        qs = qs.windowed(2) { ps -> ps[0].affine(ps[1], t) }
    return qs.first()
}