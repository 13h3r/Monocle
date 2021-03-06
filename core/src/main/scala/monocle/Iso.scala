package monocle

import org.scalacheck.Prop._
import org.scalacheck.{ Properties, Arbitrary }
import scalaz.{ Equal, Functor }

/**
 * An Iso is a Lens that can be reversed and so it defines an isomorphism.
 */
trait Iso[S, T, A, B] extends Lens[S, T, A, B] with Prism[S, T, A, B] { self =>

  def reverse: Iso[B, A, T, S]

  def re: Getter[B, T] = reverse.asGetter

  def compose[C, D](other: Iso[A, B, C, D]): Iso[S, T, C, D] = new Iso[S, T, C, D] {
    def lift[F[_]: Functor](from: S, f: C => F[D]): F[T] = self.lift(from, other.lift(_, f))
    def reverse: Iso[D, C, T, S] = other.reverse compose self.reverse
  }

}

object Iso {

  def apply[S, T, A, B](_get: S => A, _reverseGet: B => T): Iso[S, T, A, B] = new Iso[S, T, A, B] { self =>
    def reverse: Iso[B, A, T, S] = new Iso[B, A, T, S] {
      def reverse: Iso[S, T, A, B] = self

      def lift[F[_]: Functor](from: B, f: T => F[S]): F[A] =
        Functor[F].map(f(_reverseGet(from)))(_get)
    }

    def lift[F[_]: Functor](from: S, f: A => F[B]): F[T] =
      Functor[F].map(f(_get(from)))(_reverseGet)
  }

  def laws[S: Arbitrary: Equal, A: Arbitrary: Equal](iso: SimpleIso[S, A]) = new Properties("Iso") {
    import scalaz.syntax.equal._

    include(Lens.laws(iso))
    include(Prism.laws(iso))

    property("double inverse") = forAll { (from: S, newValue: A) =>
      iso.reverse.reverse.get(from) === iso.get(from)
      iso.reverse.reverse.set(from, newValue) === iso.set(from, newValue)
    }

  }

}
