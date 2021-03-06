package monocle

trait Getter[S, A] { self =>

  def get(from: S): A

  def asGetter: Getter[S, A] = self

  def compose[B](other: Getter[A, B]): Getter[S, B] = new Getter[S, B] {
    def get(from: S): B = other.get(self.get(from))
  }

}

object Getter {
  def apply[S, A](_get: S => A): Getter[S, A] = new Getter[S, A] {
    def get(from: S): A = _get(from)
  }
}
