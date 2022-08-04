package co.topl.traffic.model

import scala.util.matching.Regex

case class Intersection(
  avenue: String,
  street: String
)

object Intersection {

  def fromString(string: String): Either[Error, Intersection] = {
    val intersectionPattern: Regex = "([A-Z])(\\d+)".r
    string match {
      case intersectionPattern(avenue, street) =>
        Right(Intersection(avenue, street))
      case _ => Left(Error("Input is not a valid intersection"))
    }
  }

}
