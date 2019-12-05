import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class HelpersTest : StringSpec({
    "withValueAtIndex should replace a value at an index" {
        val result = listOf(1, 2, 3).withValueAtIndex(4, 0)
        result shouldBe listOf(4, 2, 3)
    }
})