package satorg.collection

import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FreeSpec, Matchers}

class MergeOrderedSeqTest extends FreeSpec with Matchers with GeneratorDrivenPropertyChecks {

  import MergeOrderedSeq._

  "mergeOrdered for Stream should work correctly" in {

    def streamGen =
      Gen.chooseNum(0, 10, 0, 1).
        flatMap(Gen.containerOfN[Vector, Int](_, Gen.choose(1, 10))).
        map(_.sorted.toStream)

    forAll((streamGen, "left"), (streamGen, "right")) { (left, right) =>
      val expected = (left #::: right).sorted

      val actual1 = mergeOrdered(left, right)
      val actual2 = mergeOrdered(right, left)

      actual1 should contain theSameElementsInOrderAs expected
      actual2 should contain theSameElementsInOrderAs expected
    }
  }
}
