package graph

data class Cell(val neighbors :MutableMap<Direction, Cell> =mutableMapOf(), val id :Int =++Cell.id) {
    companion object {
        var id = 0
    }

    override fun equals(other :Any?) :Boolean {
        return other is Cell && id==other.id
    }

    override fun toString() = "room$id"

    override fun hashCode() = 23 + id

    fun connect(d :Direction, c1 :Cell) {
        neighbors[d]= c1
        c1.neighbors[d.opposite()] = this
    }
}

data class Maze(val rooms :MutableList<Cell>, var center :Cell) {
    override fun toString() = "${rooms.size} rooms"

    fun describe() = toString()+":\n"+ rooms.map { r ->
        "${r.id}: "+ r.neighbors.entries.joinToString { (d :Direction, r1) -> "$d: ${r1.id}" }
    }.joinToString("\n")
}

enum class Direction {
    Up, Left, Down, Right;

    fun opposite() = when (this) {
        Up -> Down
        Right -> Left
        Down -> Up
        Left -> Right
    }

    fun next() = when (this) {
        Up -> Left
        Left -> Down
        Down -> Right
        Right -> Up
    }
}
