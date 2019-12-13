package math

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class SegmentTest : StringSpec() {

    init {
        "touches" {
            // diagonal line
            val segment = Segment(Vector(0, 0), Vector(3, 3))
            segment.touches(Vector(1, 1)) shouldBe true
            segment.touches(Vector(1, 2)) shouldBe false
            segment.touches(Vector(-1, -1)) shouldBe false

            // vertical line (should also pass for horizontal if we flip x,y)
            val verticalLine = Segment(Vector(0, 0), Vector(0, 4))
            verticalLine.touches(Vector(0, 2)) shouldBe true
            verticalLine.touches(Vector(0, -1)) shouldBe false
            verticalLine.touches(Vector(0, 5)) shouldBe false

            // other strange cases
            Segment(Vector(1, 0), Vector(3, 4)).touches(Vector(2, 2)) shouldBe true
        }
    }

}