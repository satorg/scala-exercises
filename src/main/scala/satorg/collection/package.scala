package satorg

package object collection {

  implicit final class IterableOnceExtraOps[A](private val self: IterableOnce[A]) extends AnyVal {
    @scala.annotation.tailrec
    def treeFold(z: A)(op: (A, A) => A): A = {
      self.iterator.take(2).toList match {
        case Nil => z
        case a :: Nil => a
        case as =>
          (as.iterator ++ self.iterator).
            grouped(2).
            map {
              case Seq(x) => x
              case Seq(x1, x2) => op(x1, x2)
            }.
            treeFold(z)(op)
      }
    }
  }

}
