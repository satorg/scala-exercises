package satorg.collection

import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class MergeOrderedIteratorTest extends AnyFreeSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  "it should work correctly " in {
    def listGen = Gen.chooseNum(0, 10, 0, 1).flatMap(Gen.listOfN(_, Gen.choose(1, 10))).map(_.sorted)

    forAll((listGen, "first"), (listGen, "second"), (listGen, "third"), (listGen, "fourth"), (listGen, "fifth")) {
      (first, second, third, fourth, fifth) =>
        val expected2 = (first ::: second).sorted
        val expected3 = (first ::: second ::: third).sorted
        val expected4 = (first ::: second ::: third ::: fourth).sorted
        val expected5 = (first ::: second ::: third ::: fourth ::: fifth).sorted

        val actual2 = MergeOrderedIterator(first.iterator, second.iterator).toList
        val actual3 = MergeOrderedIterator(first.iterator, second.iterator, third.iterator).toList
        val actual4 = MergeOrderedIterator(first.iterator, second.iterator, third.iterator, fourth.iterator).toList
        val actual5 = MergeOrderedIterator(first.iterator, second.iterator, third.iterator, fourth.iterator, fifth.iterator).toList

        actual2 should contain theSameElementsInOrderAs expected2
        actual3 should contain theSameElementsInOrderAs expected3
        actual4 should contain theSameElementsInOrderAs expected4
        actual5 should contain theSameElementsInOrderAs expected5
    }
  }
}
