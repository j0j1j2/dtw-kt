typealias WarpMapping = Pair<Int, Int>
typealias WarpPath = List<WarpMapping>
typealias MutableWarpPath = MutableList<WarpMapping>
typealias WarpCost = Double
typealias WindowData = Pair<Int, Int>
data class CostHistory(var cost: WarpCost, var lastI: Int, var lastJ: Int)

class DynamicTimeWarping<WarpData: TimeSeriesCompat<WarpData>> {
    fun warp(a: List<WarpData>, b: List<WarpData>, defaultWindow: WarpPath? = null): Pair<WarpCost, WarpPath> {
        val n = a.size
        val m = b.size

        val costMat = Array(n + 1) {
            Array(m + 1) {
                CostHistory(WarpCost.MAX_VALUE, -1, -1)
            }
        }
        costMat[0][0].cost = 0.0

        val window = defaultWindow ?: Array(n) { i->Array(m) { j-> Pair(i + 1, j + 1) } }.flatten()

        val path: MutableWarpPath = mutableListOf()
        var i = n
        var j = m
        while (i != 0 && j != 0) {
            path.add(Pair(i-1, j-1))
            i = costMat[i][j].lastI
            j = costMat[i][j].lastJ
        }
        path.reverse()
        return Pair(costMat[n][m].cost, path)
    }

    fun reduceByHalf(a: List<WarpData>): List<WarpData> {
        return a.windowed(2, 2, partialWindows = true) { it.average() }
    }

    fun expandResWindow(lowResPath: WarpPath, a: List<WarpData>, b: List<WarpData>, radius: Int): List<WindowData> {
        val path = lowResPath.toMutableSet()
        path.addAll(lowResPath.flatMap { (i,j) ->
            (-radius..radius).flatMap { p ->
                (-radius..radius).map { q ->
                    Pair(i + p, j + q)
                }
            }
        })

        val windowSet = path.flatMap {(i, j) ->
            listOf(
                Pair(i * 2, j * 2),
                Pair(i * 2, j * 2 + 1),
                Pair(i * 2 + 1, j * 2),
                Pair(i * 2 + 1, j * 2 + 1)
            )
        }.toSet()

        val window = mutableListOf<WindowData>()
        var startJ = 0
        for (i in 0..<a.size) {
            var newStartJ: Int? = null
            for (j in startJ..<b.size) {
                if (Pair(i, j) in windowSet) {
                    window.add(Pair(i, j))
                    if (newStartJ == null) {
                        newStartJ = j
                    }
                } else if (newStartJ != null) {
                    break
                }
            }
            startJ = newStartJ!!
        }
        return window
    }

    fun fastWarp(a: List<WarpData>, b: List<WarpData>, radius: Int = 1): Pair<WarpCost, WarpPath> {
        if (a.size < radius + 2 || b.size < radius + 2) {
            return warp(a, b)
        }
        val shrunkA = reduceByHalf(a)
        val shrunkB = reduceByHalf(b)

        val (_, lowResPath) = fastWarp(shrunkA, shrunkB, radius)

        val window =  expandResWindow(lowResPath, a, b, radius)
        return warp(a, b, window)
    }
}

