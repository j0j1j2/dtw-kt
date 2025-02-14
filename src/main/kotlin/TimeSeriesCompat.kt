interface TimeSeriesCompat<T>  {
    operator fun plus(other: T): T
    operator fun div(Int: Int): T
    operator fun minus(other: T): Double
}

fun <T> Iterable<T>.average(): T where T: TimeSeriesCompat<T> {
    var sum = this.first()
    for (element in this.drop(1)) {
        sum += element
    }
    return sum / this.count()
}