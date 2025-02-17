data class HangulChar(val initial: Char, val medial: Char, val final: Char?) {
    companion object {
        private val initialSimilarGroup = mapOf(
            *setOf('ㄱ', 'ㅋ', 'ㄲ').pairSubset().map { it to 0.8 }.toTypedArray(),
            *setOf('ㄷ', 'ㅌ', 'ㄸ').pairSubset().map { it to 0.8 }.toTypedArray(),
            *setOf('ㅂ', 'ㅍ', 'ㅃ').pairSubset().map { it to 0.8 }.toTypedArray(),
            *setOf('ㅈ', 'ㅊ', 'ㅉ').pairSubset().map { it to 0.8 }.toTypedArray(),
        )
        private val medialSimilarGroup = mapOf(
            *setOf('ㅐ', 'ㅔ').pairSubset().map { it to 1.0 }.toTypedArray(),
            *setOf('ㅒ', 'ㅖ').pairSubset().map { it to 1.0 }.toTypedArray(),
            *setOf('ㅙ', 'ㅚ', 'ㅞ').pairSubset().map { it to 1.0 }.toTypedArray(),
            *setOf('ㅡ', 'ㅣ', 'ㅢ').pairSubset().map { it to 0.8 }.toTypedArray(),
        )
        private val finalSimilarGroup = mapOf(
            *setOf('ㄱ', 'ㄲ', 'ㅋ', 'ㄺ', 'ㄳ').pairSubset().map { it to 1.0 }.toTypedArray(),
            *setOf('ㄴ', 'ㄵ', 'ㄶ').pairSubset().map { it to 1.0 }.toTypedArray(),
            *setOf('ㄷ', 'ㅌ', 'ㅅ', 'ㅆ', 'ㅈ', 'ㅊ', 'ㅎ').pairSubset().map { it to 1.0 }.toTypedArray(),
            *setOf('ㄹ',  'ㄼ', 'ㄽ', 'ㄾ', 'ㅀ').pairSubset().map { it to 1.0 }.toTypedArray(),
            *setOf('ㅂ', 'ㅍ', 'ㅄ', 'ㄿ').pairSubset().map { it to 1.0 }.toTypedArray(),
        )

        private val initials = listOf('ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')
        private val medials = listOf('ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ')
        private val finals = listOf(null, 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')
    }

    private fun groupSimilarity(group: Map<Set<Char>, Double>, a: Char?, b: Char?): Double {
        val s = setOf(a, b)
        return if (s.size <= 1) 1.0 else group[s] ?: 0.0
    }

    fun similarity(other: HangulChar?): Double {
        if (other == null) return 0.0
        val initialSimilarity = if (initial == other.initial) 1.0 else groupSimilarity(initialSimilarGroup, initial, other.initial)
        val medialSimilarity = if (medial == other.medial) 1.0 else groupSimilarity(medialSimilarGroup, medial, other.medial)
        val finalSimilarity = if (final == other.final) 1.0 else groupSimilarity(finalSimilarGroup, final, other.final)
        return (initialSimilarity + medialSimilarity + finalSimilarity) / 3.0
    }

    override fun toString(): String {
        val initialIndex = initials.indexOf(initial)
        val medialIndex = medials.indexOf(medial)
        val finalIndex = finals.indexOf(final)
        return Char(0xAC00 + initialIndex * 588 + medialIndex * 28 + finalIndex).toString()
    }
}

typealias HangulCompatChar = Either<HangulChar, Char>

object HangulUtil {
    private val initials = listOf('ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')
    private val medials = listOf('ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ')
    private val finals = listOf(null, 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')

    @JvmStatic
    fun isCompositeHangul(ch: Char): Boolean {
        return ch.code in 0xAC00..0xD7A3
    }

    @JvmStatic
    fun dissectHangul(ch: Char): HangulCompatChar {
        if (!isCompositeHangul(ch)) return Either.Right(ch)

        val offset = ch.code - 0xAC00
        val initialIndex = offset / (medials.size * finals.size)
        val medialIndex = (offset % (medials.size * finals.size)) / finals.size
        val finalIndex = offset % finals.size

        val initial = initials[initialIndex]
        val medial = medials[medialIndex]
        val final = if (finalIndex in finals.indices) finals[finalIndex] else null
        return  Either.Left(HangulChar(initial, medial, final))
    }

    @JvmStatic
    fun hangulStrSimilarity(lhs: String, rhs: String): Pair<WarpCost, WarpPath> {
        val lhsSeries = timeSeriesHangulListOf(*lhs.map { dissectHangul(it) }.toTypedArray())
        val rhsSeries = timeSeriesHangulListOf(*rhs.map { dissectHangul(it) }.toTypedArray())
        val dtw = DynamicTimeWarping<TimeSeriesHangul>()
        return dtw.warp(lhsSeries, rhsSeries)
    }
}