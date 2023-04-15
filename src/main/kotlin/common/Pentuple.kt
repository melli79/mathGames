package common

data class Pentuple<R,S,T,U,V>(val first :R, val second :S, val third :T, val fourth :U, val fifth :V) {
    override fun toString() = "Pentuple($first, $second, $third, $fourth, $fifth)"
}
