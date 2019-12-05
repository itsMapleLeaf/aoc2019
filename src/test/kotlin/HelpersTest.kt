import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class HelpersTest : StringSpec({
    "replace should replace a value at an index" {
        listOf(1, 2, 3).replace(4, 0) shouldBe listOf(4, 2, 3)
    }
})