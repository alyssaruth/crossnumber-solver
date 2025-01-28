package maths

import io.kotest.matchers.shouldBe
import java.util.Calendar
import kotlin.test.Test

class MathsTest {
    @Test
    fun `Should report the correct day of the week for a given date`() {
        dayOfWeek("2009-08-13") shouldBe Calendar.THURSDAY
        dayOfWeek("2025-01-28") shouldBe Calendar.TUESDAY
        dayOfWeek("2025-01-01") shouldBe Calendar.WEDNESDAY
    }
}