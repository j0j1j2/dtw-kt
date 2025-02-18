import kotlin.math.abs

typealias WarpMapping = Pair<Int, Int>
typealias WarpPath = List<WarpMapping>
typealias MutableWarpPath = MutableList<WarpMapping>
typealias WarpCost = Double
typealias WindowData = Pair<Int, Int>
typealias CostPosition = Pair<Int, Int>
data class CostHistory(var cost: WarpCost, var prevI: Int, var prevJ: Int, var streak: Int = 0)

class DynamicTimeWarping<WarpData> where WarpData: TimeSeriesCompat<WarpData> {
    fun warp(a: List<WarpData>, b: List<WarpData>, defaultWindow: WarpPath? = null): Triple<WarpCost, WarpPath, Array<Array<CostHistory>>> {
        val n = a.size
        val m = b.size

        val costMat = Array(n + 1) {
            Array(m + 1) {
                CostHistory(WarpCost.MAX_VALUE, -1, -1)
            }
        }
        costMat[0][0].cost = 0.0

        val window = defaultWindow ?: (1..n).flatMap {i->
            (1..m).map { j->
                Pair(i, j)
            }
        }

        for ((i, j) in window) {
            val prevCandidates = listOf(CostPosition(i - 1, j), CostPosition(i, j - 1), CostPosition(i - 1, j - 1))
            val (pos, prev) = prevCandidates.map { (y, x) -> Pair(CostPosition(y, x),costMat[y][x]) }
                .sortedWith( compareBy<Pair<CostPosition, CostHistory>> { it.second.cost }.thenByDescending { it.second.streak })
                .first()

            pos.let {(y, x) ->
                val distance = abs(a[i-1] - b[j-1])
                costMat[i][j].cost = distance + costMat[y][x].cost
                costMat[i][j].streak = if (distance == .0) prev.streak + 1 else 0
                costMat[i][j].prevI = y
                costMat[i][j].prevJ = x
            }
        }
        val path: MutableWarpPath = mutableListOf()
        var i = n
        var j = m
        while (i != 0 && j != 0) {
            path.add(Pair(i-1, j-1))
            costMat[i][j].let {
                i = it.prevI
                j = it.prevJ
            }
        }
        path.reverse()
        return Triple(costMat[n][m].cost, path, costMat)
    }

    fun reduceByHalf(a: List<WarpData>): List<WarpData> {
        return a.windowed(2, 2, partialWindows = true) { it.average() }
    }

    fun expandResWindow(lowResPath: WarpPath, a: List<WarpData>, b: List<WarpData>, radius: Int): List<WindowData> {
        val path = lowResPath.toMutableSet()
        path.addAll(lowResPath.flatMap { (i,j) ->
            (-radius..radius).flatMap { p ->
                (-radius..radius).mapNotNull { q ->
                    if (i+p>=0 && j+q>=0)  Pair(i + p, j + q) else null
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
        for (i in a.indices) {
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
            if (newStartJ != null) {
                startJ = newStartJ
            }
        }
        return window.map { (i, j) -> Pair(i + 1, j + 1) }
    }

    fun fastWarp(a: List<WarpData>, b: List<WarpData>, radius: Int = 1): Triple<WarpCost, WarpPath, Array<Array<CostHistory>>> {
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

