package satorg.collection

import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class MergeOrderedIteratorTest extends AnyFreeSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  "it should work correctly" in {
    def listGen = Gen.chooseNum(0, 10, 0, 1).flatMap(Gen.listOfN(_, Gen.choose(1, 10))).map(_.sorted)

    forAll((listGen, "left"), (listGen, "right")) { (left, right) =>
      val expected = (left ::: right).sorted

      val actual1 = MergeOrderedIterator(left.iterator, right.iterator).toList
      val actual2 = MergeOrderedIterator(right.iterator, left.iterator).toList

      actual1 should contain theSameElementsInOrderAs expected
      actual2 should contain theSameElementsInOrderAs expected
    }
  }
}
