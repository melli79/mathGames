package common

data class Quadruple<R,S,T,U>(val first :R, val second :S, val third :T, val fourth :U) {
    override fun toString() = "Quadruple($first, $second, $third, $fourth)"
}