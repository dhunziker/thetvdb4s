package thetvdb4s

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.circe._
import org.http4s.client.blaze.Http1Client
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._


object Search {
  case class SeriesSearchResults(data: List[SeriesSearchResult])
  case class SeriesSearchResult(aliases: List[String],
                                banner: String,
                                firstAired: String,
                                id: Int,
                                network: String,
                                overview: String,
                                seriesName: String,
                                status: String)
  case class EpisodeDataQueryParams(params: List[String])

  def searchSeries(value: String, param: String = "name")(implicit ctx: Context): IO[SeriesSearchResults] = {
    val req = GET(ctx.baseUri / "search" / "series"
      withQueryParam(param, value),
      ctx.acceptHeader,
      ctx.acceptLanguageHeader,
      ctx.authHeader)
    Http1Client[IO]().flatMap { httpClient =>
      httpClient.expect(req)(jsonOf[IO, SeriesSearchResults])
    }
  }

  def searchSeriesParams(implicit ctx: Context): IO[EpisodeDataQueryParams] = {
    val req = GET(ctx.baseUri / "search" / "series" / "params", ctx.acceptHeader, ctx.authHeader)
    Http1Client[IO]().flatMap { httpClient =>
      implicit val dec = dataDecoder[EpisodeDataQueryParams]
      httpClient.expect(req)(jsonOf[IO, EpisodeDataQueryParams])
    }
  }
}
