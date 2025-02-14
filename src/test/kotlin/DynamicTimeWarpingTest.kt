import TimeSeriesDouble
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DynamicTimeWarpingTest {
    @Test
    fun testFastWarp() {
        val dtw = DynamicTimeWarping<TimeSeriesDouble>()
        val a = timeSeriesDoubleListOf(1.0, 2.0, 3.0, 4.0, 5.0)
        val b = timeSeriesDoubleListOf(2.0, 3.0, 4.0)

        val (cost, path) = dtw.fastWarp(a, b)
        assertEquals(2.0, cost)
        assertEquals(listOf(Pair(0, 0), Pair(1, 0), Pair(2, 1), Pair(3, 2), Pair(4, 2)), path)
    }

    @Test
    fun testWarp() {
        val dtw = DynamicTimeWarping<TimeSeriesDouble>()
        val a = timeSeriesDoubleListOf(1.0, 2.0, 3.0, 4.0, 5.0)
        val b = timeSeriesDoubleListOf(2.0, 3.0, 4.0)

        val (cost, path) = dtw.warp(a, b)
        assertEquals(2.0, cost)
        assertEquals(listOf(Pair(0, 0), Pair(1, 0), Pair(2, 1), Pair(3, 2), Pair(4, 2)), path)
    }
}