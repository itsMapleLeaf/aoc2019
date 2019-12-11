package math

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class SegmentTest : StringSpec() {

    init {
        "touches" {
            val segment = Segment(Point(0, 0), Point(3, 3))

            segment.touches(Point(1, 1)) shouldBe true
            segment.touches(Point(1, 2)) shouldBe false
            segment.touches(Point(-1, -1)) shouldBe false
        }
    }

}