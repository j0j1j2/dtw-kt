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

/**
 * Time series compatible Double
 */
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

/**
 * Time series compatible Hangul character
 */
class TimeSeriesHangul(private val ch: HangulCompatChar): TimeSeriesCompat<TimeSeriesHangul> {
    override fun div(value: Int): TimeSeriesHangul {
        throw NotImplementedError()
    }

    override fun plus(other: TimeSeriesHangul): TimeSeriesHangul {
        throw NotImplementedError()
    }

    override fun minus(other: TimeSeriesHangul): Double {
        return 1.0 - when (this.ch) {
            is Either.Left -> this.ch.value.similarity(other.ch.left())
            is Either.Right -> this.ch.value.similarity(other.ch.right())
        }
    }
}

fun timeSeriesHangulListOf(vararg values: HangulCompatChar): List<TimeSeriesHangul> {
    return values.map { TimeSeriesHangul(it) }
}

fun timeSeriesHangulMutableListOf(vararg values: HangulCompatChar): MutableList<TimeSeriesHangul> {
    return values.map { TimeSeriesHangul(it) }.toMutableList()
}

fun timeSeriesHangulSetOf(vararg values: HangulCompatChar): Set<TimeSeriesHangul> {
    return values.map { TimeSeriesHangul(it) }.toSet()
}

fun timeSeriesHangulMutableSetOf(vararg values: HangulCompatChar): MutableSet<TimeSeriesHangul> {
    return values.map { TimeSeriesHangul(it) }.toMutableSet()
}