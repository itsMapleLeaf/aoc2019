import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec

class HelpersTest : StringSpec({
    "replace should replace a value at an index" {
        listOf(1, 2, 3).replace(4, 0) shouldBe listOf(4, 2, 3)
    }

    "plusAt" {
        listOf(1, 2, 4, 5).plusAt(2, 3) shouldBe listOf(1, 2, 3, 4, 5)
        listOf(2, 3).plusAt(0, 1) shouldBe listOf(1, 2, 3)
        listOf(1, 2).plusAt(2, 3) shouldBe listOf(1, 2, 3)
        shouldThrow<Error> { listOf(1, 2, 3).plusAt(10, 69) }
    }
})