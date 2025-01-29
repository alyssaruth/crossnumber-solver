package maths

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PrimesTest {
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

    @Test
    fun `Should correctly prime factorise`() {
        19L.primeFactors() shouldBe listOf(19)
        1024L.primeFactors() shouldBe listOf(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
        78204L.primeFactors() shouldBe listOf(2, 2, 3, 7, 7, 7, 19)

        42125857312073.primeFactors() shouldBe listOf(6328831, 6656183)
    }

    @Test
    fun `Should correctly find the smallest prime factor`() {
        firstPrimeFactor(35) shouldBe 5
        firstPrimeFactor(42125857312073) shouldBe 6328831
    }

    @Test
    fun `Should find the next prime after a given number`() {
        nextPrime(1) shouldBe 2
        nextPrime(2) shouldBe 3
        nextPrime(3) shouldBe 5
        nextPrime(4) shouldBe 5
        nextPrime(5) shouldBe 7
        nextPrime(6) shouldBe 7
        nextPrime(7) shouldBe 11
        nextPrime(8) shouldBe 11

        nextPrime(370262) shouldBe 370373
    }

    @Test
    fun `Should find all primes up to a given limit`() {
        primesUpTo(2) shouldBe listOf(2)
        primesUpTo(3) shouldBe listOf(2, 3)
        primesUpTo(10) shouldBe listOf(2, 3, 5, 7)
        primesUpTo(50) shouldBe listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47)
    }

    @Test
    fun `Should count primes up to a limit`() {
        countPrimesUpTo(2) shouldBe 1
        countPrimesUpTo(10) shouldBe 4

        countPrimesUpTo(10_000_000) shouldBe 664579
    }

    @Test
    fun `Should count twin primes up to a limit`() {
        countTwinPrimesUpTo(100) shouldBe 8
        countTwinPrimesUpTo(1000) shouldBe 35
        countTwinPrimesUpTo(10000) shouldBe 205
        countTwinPrimesUpTo(100000) shouldBe 1224
        countTwinPrimesUpTo(1_000_000) shouldBe 8169
    }

    @Test
    fun `Should identfy Fermat primes`() {
        primesUpTo(100000).filter(::isFermatPrime) shouldContainExactly listOf(3, 5, 17, 257, 65537)
    }

    @Test
    fun `Should identify sums of consecutive primes`() {
        isSumOfConsecutivePrimes(3, 2)(11 + 13 + 17) shouldBe true
        isSumOfConsecutivePrimes(3, 2)(11 + 13 + 15) shouldBe false
        isSumOfConsecutivePrimes(7, 3)(947) shouldBe true
    }
}