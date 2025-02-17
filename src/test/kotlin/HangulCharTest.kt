import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class HangulCharTest {

    @Test
    fun testSimilarity() {
        assertThat(HangulChar('ㄱ', 'ㅏ', null).similarity(HangulChar('ㄱ', 'ㅏ', null))).isEqualTo(1.0)
        assertThat(HangulChar('ㄱ', 'ㅏ', null).similarity(HangulChar('ㄱ', 'ㅏ', 'ㄱ'))).isEqualTo(2.0/3.0)
        assertThat(HangulChar('ㄱ', 'ㅏ', null).similarity(HangulChar('ㄱ', 'ㅏ', 'ㄲ'))).isEqualTo(2.0/3.0)
        assertThat(HangulChar('ㄱ', 'ㅏ', null).similarity(null)).isEqualTo(0.0)
    }

    @Test
    fun testToString() {
        assertThat(HangulChar('ㄱ', 'ㅏ', null).toString()).isEqualTo("가")
        assertThat(HangulChar('ㅎ', 'ㅣ', 'ㅎ').toString()).isEqualTo("힣")
    }
}