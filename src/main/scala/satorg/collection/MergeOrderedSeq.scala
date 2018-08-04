package satorg.collection

import scala.annotation.tailrec

object MergeOrderedSeq {

  def mergeOrdered[A: Ordering](left: Stream[A], right: Stream[A]): Stream[A] = {

    (left, right) match {
      case (Stream.Empty, _) => right
      case (_, Stream.Empty) => left

      case (leftHead #:: leftTail, rightHead #:: rightTail) =>
        val ordering = implicitly[Ordering[A]]

        if (ordering.lt(leftHead, rightHead)) {
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
        val ordering = implicitly[Ordering[A]]

        if (ordering.lt(leftHead, rightHead)) {
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
