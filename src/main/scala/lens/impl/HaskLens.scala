package lens.impl

import scalaz.Functor
import lens.Lens
import util.{Constant, Identity}


trait HaskLens[A, B] extends Lens[A,B] {
  protected def lensFunction[F[_] : Functor](lift: B => F[B], a: A): F[A]

  def get(a: A): B = {
    val b2Fb: B => Constant[B, B] = { b: B => Constant(b)}
    lensFunction[({type l[a] = Constant[B,a]})#l] (b2Fb, a).value
  }

  def modify(from: A, f: B => B): A = {
    val b2Fb: B => Identity[B] = { b : B => Identity(f(b)) }
    lensFunction[Identity](b2Fb, from).value
  }

  // overload
  def >-[C](other: HaskLens[B, C]): Lens[A, C] = HaskLens.compose(this, other)
}

object HaskLens {

  def compose[A, B, C](a2b: HaskLens[A, B], b2C: HaskLens[B, C]): HaskLens[A,C] = new HaskLens[A, C] {
    // (b -> f b) -> a -> f a  and (c -> f c) -> b -> f b
    // (c -> f c) -> a -> f a
    protected def lensFunction[F[_] : Functor](lift: C => F[C], a: A): F[A] =
      a2b.lensFunction({b: B => b2C.lensFunction(lift, b)}, a)
  }


}




