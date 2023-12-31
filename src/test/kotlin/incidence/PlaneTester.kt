package incidence

import kotlin.test.*

class PlaneTester {
    @Test fun createLine() {
        val plane = Plane(7u)
        val p = Point2D(plane, 1u, 1u)
        val q = Point2D(plane, 2u, 3u)
        val line = Line2D.of(p, q)
        assertNotNull(line)
        val result = p in line
        println("Point $p is ${if (result) "" else "not "}in Line $line.")
        assertTrue(result)
        assertTrue(q in line)
    }

    @Test fun incidence() {
        val plane = Plane(5u)
        val p = Point2D(plane, 1u, 2u)
        assertTrue(p in plane)
        val line = Line2D.of(p, Point2D(plane, 2u, 3u))
        assertNotNull(line)
        assertTrue(line in plane)
    }
}
