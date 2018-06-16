package thetvdb4s

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.{AuthScheme, Credentials}
import org.http4s.circe._
import org.http4s.client.blaze.Http1Client
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._
import org.http4s.headers.Authorization

object Authentication {
  case class Token(token: String)
  case class Auth(apikey: String,
                  userkey: String,
                  username: String)

  def login(auth: Auth, ctx: Context = Context()): IO[Context] = {
    val req = POST(ctx.baseUri / "login", auth.asJson, ctx.acceptHeader)
    Http1Client[IO]().flatMap { httpClient =>
      httpClient.expect(req)(jsonOf[IO, Token]).map(ctx.updateToken)
    }
  }

  def refresh(implicit ctx: Context): IO[Context] = withToken { token =>
    val req = GET(ctx.baseUri / "refresh_token", ctx.acceptHeader,
      Authorization(Credentials.Token(AuthScheme.Bearer, token.token)))
    Http1Client[IO]().flatMap { httpClient =>
      httpClient.expect(req)(jsonOf[IO, Token]).map(ctx.updateToken)
    }
  }
}
