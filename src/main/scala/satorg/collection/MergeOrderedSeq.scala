package satorg.collection

import scala.annotation.tailrec

object MergeOrderedSeq {

  import Ordering.Implicits._

  def mergeOrdered[A: Ordering](left: LazyList[A], right: LazyList[A]): LazyList[A] = {

    (left, right) match {
      case _ if left.isEmpty => right
      case _ if right.isEmpty => left

      case (leftHead #:: leftTail, rightHead #:: rightTail) =>
        if (leftHead < rightHead) {
          leftHead #:: mergeOrdered(leftTail, right)
        }
        else {
          rightHead #:: mergeOrdered(left, rightTail)
        }
    }
  }

  @tailrec
  private def mergeOrderedR[A: Ordering](left: List[A], right: List[A], acc: List[A]): List[A] = {

    (left, right) match {
      case (Nil, _) => right reverse_::: acc
      case (_, Nil) => left reverse_::: acc
      case (leftHead :: leftTail, rightHead :: rightTail) =>
        if (leftHead < rightHead) {
          mergeOrderedR(leftTail, right, leftHead :: acc)
        }
        else {
          mergeOrderedR(left, rightTail, rightHead :: acc)
        }
    }
  }

  def mergeOrdered[A: Ordering](left: List[A], right: List[A]): List[A] = {

    mergeOrderedR(left, right, Nil).reverse
  }
}
