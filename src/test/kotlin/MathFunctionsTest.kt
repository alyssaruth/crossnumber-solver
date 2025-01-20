import com.github.alyssaruth.isPrime
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MathFunctionsTest {
    @Test
    fun `Should correctly (and performantly) check prime numbers`() {
        isPrime(2) shouldBe true
        isPrime(3) shouldBe true
        isPrime(5) shouldBe true
        isPrime(7) shouldBe true
        isPrime(11) shouldBe true
        isPrime(2772403811878331) shouldBe true

        isPrime(1) shouldBe false
        isPrime(4) shouldBe false
        isPrime(9) shouldBe false
        isPrime(2772403811878333) shouldBe false
    }
}