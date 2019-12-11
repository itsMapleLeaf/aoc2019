package math

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class SegmentTest : StringSpec() {

    init {
        "touches" {
            // diagonal line
            val segment = Segment(Point(0, 0), Point(3, 3))
            segment.touches(Point(1, 1)) shouldBe true
            segment.touches(Point(1, 2)) shouldBe false
            segment.touches(Point(-1, -1)) shouldBe false

            // vertical line (should also pass for horizontal if we flip x,y)
            val verticalLine = Segment(Point(0, 0), Point(0, 4))
            verticalLine.touches(Point(0, 2)) shouldBe true
            verticalLine.touches(Point(0, -1)) shouldBe false
            verticalLine.touches(Point(0, 5)) shouldBe false

            // other strange cases
            Segment(Point(1, 0), Point(3, 4)).touches(Point(2, 2)) shouldBe true
        }
    }

}