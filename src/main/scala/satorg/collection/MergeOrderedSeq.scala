package satorg.collection

object MergeOrderedSeq {

  def mergeOrdered[A: Ordering](left: Stream[A], right: Stream[A]): Stream[A] = {

    (left, right) match {
      case (Stream.Empty, Stream.Empty) => Stream.Empty
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
}
