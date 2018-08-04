package satorg.collection

import scala.collection.AbstractIterator

object MergeOrderedIterator {

  private final class PreviewableIterator[A](iterator: Iterator[A]) extends AbstractIterator[A] {

    private var hasPreview: Boolean = false
    private var preview: A = _

    def hasNext: Boolean = { hasPreview || iterator.hasNext }

    def head: A = {
      if (!hasPreview) {
        preview = iterator.next()
        hasPreview = true
      }
      preview
    }

    def next(): A = {
      if (hasPreview) {
        hasPreview = false
        preview
      }
      else {
        iterator.next()
      }
    }
  }

  def mergeOrdered[A: Ordering](left: Iterator[A], right: Iterator[A]): Iterator[A] =
    new MergeOrderedIterator(left, right)
}

import satorg.collection.MergeOrderedIterator._

final class MergeOrderedIterator[+A: Ordering] private(left: PreviewableIterator[A],
                                                       right: PreviewableIterator[A])
  extends AbstractIterator[A] {

  def this(leftIt: Iterator[A], rightIt: Iterator[A]) =
    this(
      new PreviewableIterator(leftIt),
      new PreviewableIterator(rightIt))

  override def hasNext: Boolean = { left.hasNext || right.hasNext }

  override def next(): A = {

    (left.hasNext, right.hasNext) match {
      case (false, false) => Iterator.empty.next() // raise an error
      case (true, false) => left.next()
      case (false, true) => right.next()
      case _ =>
        val ordering = implicitly[Ordering[A]]

        if (ordering.lt(left.head, right.head)) {
          left.next()
        }
        else {
          right.next()
        }
    }
  }
}
