package satorg.collection

import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FreeSpec, Matchers}


class MergeOrderedIteratorTest extends FreeSpec with Matchers with GeneratorDrivenPropertyChecks {

  "it should work correctly" in {
    def listGen = Gen.chooseNum(0, 10, 0, 1).flatMap(Gen.listOfN(_, Gen.choose(1, 10))).map(_.sorted)

    forAll((listGen, "left"), (listGen, "right")) { (left, right) =>
      val expected = (left ::: right).sorted

      val actual1 = new MergeOrderedIterator(left.iterator, right.iterator).toList
      val actual2 = new MergeOrderedIterator(right.iterator, left.iterator).toList

      actual1 should contain theSameElementsInOrderAs expected
      actual2 should contain theSameElementsInOrderAs expected
    }
  }
}
