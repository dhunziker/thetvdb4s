import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import thetvdb4s.Authentication.Token

package object thetvdb4s {
  case class Data[A](data: A)
  def dataDecoder[A](implicit dec: Decoder[A]): Decoder[A] = deriveDecoder[Data[A]].map(_.data)

  def withToken[A](f: Token => A)(implicit ctx: Context): A = {
    ctx.validate()
    f.apply(ctx.token.get)
  }
}
