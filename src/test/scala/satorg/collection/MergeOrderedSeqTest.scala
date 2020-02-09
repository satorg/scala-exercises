package satorg.collection

import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class MergeOrderedSeqTest extends AnyFreeSpec with Matchers with ScalaCheckDrivenPropertyChecks {

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

  "mergeOrdered for List should work correctly" in {

    def listGen =
      Gen.chooseNum(0, 10, 0, 1).
        flatMap(Gen.listOfN[Int](_, Gen.choose(1, 10))).
        map(_.sorted)

    forAll((listGen, "left"), (listGen, "right")) { (left, right) =>
      val expected = (left ::: right).sorted

      val actual1 = mergeOrdered(left, right)
      val actual2 = mergeOrdered(right, left)

      actual1 should contain theSameElementsInOrderAs expected
      actual2 should contain theSameElementsInOrderAs expected
    }
  }
}
