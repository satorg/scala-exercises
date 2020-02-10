package satorg.collection

import scala.collection.AbstractIterator

object MergeOrderedIterator {

  def apply[A: Ordering](first: Iterator[A], second: Iterator[A], others: Iterator[A]*): Iterator[A] = {
    MergeOrderedIterator(Iterator(first, second) ++ others.iterator)
  }

  @scala.annotation.tailrec
  private def apply[A: Ordering](iters: Iterator[Iterator[A]]): Iterator[A] = {
    iters.take(2).toList match {
      case Nil => Iterator.empty
      case single :: Nil => single
      case many =>
        val mergeIters =
          (many.iterator ++ iters).grouped(2).map {
            case Seq(single) => single
            case Seq(first, second) => new MergeOrderedIterator(first, second)
          }

        MergeOrderedIterator(mergeIters)
    }
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
