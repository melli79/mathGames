package common

fun <T> MutableList<T>.removeFrom(start :Int) {
    while (size>start)
        removeLast()
}