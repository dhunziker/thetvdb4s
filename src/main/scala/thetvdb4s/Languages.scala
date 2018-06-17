package thetvdb4s

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.circe.{jsonOf, _}
import org.http4s.client.blaze.Http1Client
import org.http4s.client.dsl.io._
import org.http4s.dsl.io.GET

object Languages {
  case class LanguageData(data: List[Language])
  case class Language(abbreviation: String,
                      englishName: String,
                      id: Int,
                      name: String)

  def languages(implicit ctx: Context): IO[LanguageData] = {
    val req = GET(ctx.baseUri / "languages", ctx.acceptHeader, ctx.authHeader)
    Http1Client[IO]().flatMap { httpClient =>
      httpClient.expect(req)(jsonOf[IO, LanguageData])
    }
  }

  def languages(id: String)(implicit ctx: Context): IO[Language] = {
    val req = GET(ctx.baseUri / "languages" / id, ctx.acceptHeader, ctx.authHeader)
    implicit val dec = dataDecoder[Language]
    Http1Client[IO]().flatMap { httpClient =>
      httpClient.expect(req)(jsonOf[IO, Language])
    }
  }
}
