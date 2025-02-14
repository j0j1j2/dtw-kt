interface TimeSeriesCompat<T>  {
    operator fun plus(other: T): T
    operator fun div(value: Int): T
    operator fun minus(other: T): Double
}

fun <T> Iterable<T>.average(): T where T: TimeSeriesCompat<T> {
    var sum = this.first()
    for (element in this.drop(1)) {
        sum += element
    }
    return sum / this.count()
}

class TimeSeriesDouble(private val value: Double): TimeSeriesCompat<TimeSeriesDouble> {
    override fun plus(other: TimeSeriesDouble): TimeSeriesDouble {
        return TimeSeriesDouble(this.value + other.value)
    }

    override fun div(value: Int): TimeSeriesDouble {
        return TimeSeriesDouble(this.value / value)
    }

    override fun minus(other: TimeSeriesDouble): Double {
        return this.value - other.value
    }
}

fun timeSeriesDoubleListOf(vararg values: Double): List<TimeSeriesDouble> {
    return values.map { TimeSeriesDouble(it) }
}

fun timeSeriesDoubleMutableListOf(vararg values: Double): MutableList<TimeSeriesDouble> {
    return values.map { TimeSeriesDouble(it) }.toMutableList()
}

fun timeSeriesDoubleSetOf(vararg values: Double): Set<TimeSeriesDouble> {
    return values.map { TimeSeriesDouble(it) }.toSet()
}

fun timeSeriesDoubleMutableSetOf(vararg values: Double): MutableSet<TimeSeriesDouble> {
    return values.map { TimeSeriesDouble(it) }.toMutableSet()
}