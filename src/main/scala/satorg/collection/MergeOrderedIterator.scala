package satorg.collection

import scala.collection.AbstractIterator

object MergeOrderedIterator {

  def apply[A: Ordering](left: Iterator[A], right: Iterator[A]): Iterator[A] =
    new MergeOrderedIterator(left, right)
}

final class MergeOrderedIterator[+A: Ordering] private(
  left: collection.BufferedIterator[A],
  right: collection.BufferedIterator[A])
  extends AbstractIterator[A] {

  def this(leftIt: Iterator[A], rightIt: Iterator[A]) = this(leftIt.buffered, rightIt.buffered)

  override def hasNext: Boolean = { left.hasNext || right.hasNext }

  override def next(): A = {
    import Ordering.Implicits._

    (left.hasNext, right.hasNext) match {
      case (false, false) => Iterator.empty.next() // raise an error
      case (true, false) => left.next()
      case (false, true) => right.next()
      case _ =>
        if (left.head < right.head)
          left.next()
        else
          right.next()
    }
  }
}
