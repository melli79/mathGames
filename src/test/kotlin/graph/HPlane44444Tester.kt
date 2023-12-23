package graph

import kotlin.test.*

class HPlane44444Tester {
    @BeforeTest fun init() {
        Cell.id = 0
    }

    @Test fun center() {
        val g = generate44444graph(0u)
        println(g.describe())
        assertEquals(1u, g.numVertices)
        assertEquals(emptyList(), g.getEdges())
    }

    @Test fun trivialMaze() {
        val m = generate44444maze(0u)
        println(m.describe())
        assertEquals(1, m.rooms.size)
        assertEquals(emptyMap(), m.center.neighbors)
    }

    @Test fun firstLayer() {
        val g= generate44444graph(1u)
        assertEquals((1+4*3).toUInt(), g.numVertices)
        println(g.describe())
    }

    @Test fun layer1Maze() {
        val m = generate44444maze(1u)
        println(m.describe())
        val expected = generate44444graph(1u)
        assertEquals(expected.numVertices, m.rooms.size.toUInt())
        for (r in m.rooms) {
            val v = r.id-1
            val enn = expected.findNeighbors(v).size
            assertEquals(enn, r.neighbors.size, "Neighbors of $r differ: expected: $enn, actual: ${r.neighbors} (${r.neighbors.size})")
        }
    }

    @Test fun secondLayer() {
        val g = generate44444graph(2u)
        assertEquals((1+4*3*(1+3)).toUInt(), g.numVertices)
        println(g.describe())
    }

    @Test fun layer2Maze() {
        val m = generate44444maze(2u)
        println(m.describe())
        val expected = generate44444graph(2u)
        assertEquals(expected.numVertices, m.rooms.size.toUInt())
        for (r in m.rooms) {
            val v = r.id-1
            val enn = expected.findNeighbors(v).size
            assertEquals(enn, r.neighbors.size, "Neighbors of $r differ: expected: $enn, actual: ${r.neighbors} (${r.neighbors.size})")
        }
    }

    @Test fun lowestLayers() {
        for (n in 0..5) {
            val m = generate44444maze(n.toUShort())
            println("$n: |V|= %4d, center: ${m.center.neighbors}, EE neighs = %.4f".format(m.rooms.size,
                m.rooms.sumOf { it.neighbors.size.toLong() }.toDouble()/m.rooms.size))
        }
    }

    @Test fun circ3() {
        val m = generate44444maze(3u)
        val c = m.center
        var rim = Direction.entries.map { Pair(c, it) }.toSet()
        for (level in 1..3) {
            rim = rim.flatMap { (c :Cell, d :Direction) ->
                Direction.entries.filter { it!=d && it in c.neighbors.keys }.map {  Pair(c.neighbors[it]!!, it) }
            }.toSet()
        }
        val cells = rim.map { it.first }.toSet()
        println("In 3 steps you reach ${cells.size} Cells.")
    }
}
