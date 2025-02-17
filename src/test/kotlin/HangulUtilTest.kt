import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HangulUtilTest {

    @Test
    fun isCompositeHangul() {
        assertThat(HangulUtil.isCompositeHangul('가')).isTrue()
        assertThat(HangulUtil.isCompositeHangul('힣')).isTrue()
        assertThat(HangulUtil.isCompositeHangul('a')).isFalse()
        assertThat(HangulUtil.isCompositeHangul('ㄴ')).isFalse()
    }

    @Test
    fun dissectHangul() {
        assertThat(HangulUtil.dissectHangul('가'))
            .isEqualTo(Either.Left(HangulChar('ㄱ', 'ㅏ', null)))

        assertThat(HangulUtil.dissectHangul('힣'))
            .isEqualTo(Either.Left(HangulChar('ㅎ', 'ㅣ', 'ㅎ')))

        assertThat(HangulUtil.dissectHangul('뱙'))
            .isEqualTo(Either.Left(HangulChar('ㅂ', 'ㅑ', 'ㄾ')))

        assertThat(HangulUtil.dissectHangul('a'))
            .isEqualTo(Either.Right('a'))
    }

    @Test
    fun hangulStrSimilarity() {
        val (cost, path) = HangulUtil.hangulStrSimilarity("가나다", "가나다")
        assertThat(cost).isEqualTo(0.0)
        assertThat(path).containsExactly(
            Pair(0, 0),
            Pair(1, 1),
            Pair(2, 2),
        )

    }
}