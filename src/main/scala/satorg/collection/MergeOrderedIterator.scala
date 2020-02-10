package satorg.collection

import scala.collection.AbstractIterator

object MergeOrderedIterator {

  def apply[A: Ordering](first: Iterator[A], second: Iterator[A], others: Iterator[A]*): Iterator[A] = {
    (Iterator(first, second) ++ others.iterator).
      treeFold(Iterator.empty)(new MergeOrderedIterator(_, _))
  }
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
