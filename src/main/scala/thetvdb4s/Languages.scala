package thetvdb4s

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.{AuthScheme, Credentials}
import org.http4s.circe.{jsonOf, _}
import org.http4s.client.blaze.Http1Client
import org.http4s.client.dsl.io._
import org.http4s.dsl.io.GET
import org.http4s.headers.Authorization

object Languages {
  case class LanguageData(data: List[Language])
  case class Language(abbreviation: String,
                      englishName: String,
                      id: Int,
                      name: String)

  def languages(implicit ctx: Context): IO[LanguageData] = withToken { token =>
    val req = GET(ctx.baseUri / "languages", ctx.acceptHeader,
      Authorization(Credentials.Token(AuthScheme.Bearer, token.token)))
    Http1Client[IO]().flatMap { httpClient =>
      httpClient.expect(req)(jsonOf[IO, LanguageData])
    }
  }

  def languages(id: String)(implicit ctx: Context): IO[Language] = withToken { token =>
    val req = GET(ctx.baseUri / "languages" / id, ctx.acceptHeader,
      Authorization(Credentials.Token(AuthScheme.Bearer, token.token)))
    implicit val dec = dataDecoder[Language]
    Http1Client[IO]().flatMap { httpClient =>
      httpClient.expect(req)(jsonOf[IO, Language])
    }
  }
}
