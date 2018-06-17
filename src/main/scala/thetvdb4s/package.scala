import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

package object thetvdb4s {
  case class Data[A](data: A)

  def dataDecoder[A](implicit dec: Decoder[A]): Decoder[A] = deriveDecoder[Data[A]].map(_.data)
}
