Monocle
=======

[![Build Status](https://api.travis-ci.org/julien-truffaut/Monocle.png?branch=master)](https://travis-ci.org/julien-truffaut/Monocle)

Monocle is a Scala lens library greatly inspired by Haskell [Lens](https://github.com/ekmett/lens)

Sub projects:

Core contains the main library concepts: Lens, Traversal, Prism, Iso, Getter and Setter.
Core only depends on [scalaz](https://github.com/scalaz/scalaz) for type classes and [scalacheck](http://www.scalacheck.org/) to encode laws.

Generic is an experiment to provide highly generalised Lens and Iso using HList from [shapeless](https://github.com/milessabin/shapeless).
Generic focus is on neat abstraction but that may come at additional runtime or compile time cost.

Example shows how other sub projects can be used.


```scala
resolvers ++= Seq(
  "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "com.github.julien-truffaut"  %%  "monocle-core"  % "0.1" // or 0.2-SNAPSHOT
)
```