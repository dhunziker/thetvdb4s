package thetvdb4s

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.circe._
import org.http4s.client.blaze.Http1Client
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._

object Series {
  case class SeriesEpisodesQuery(data: List[BasicEpisode],
                                 errors: Option[JSONErrors],
                                 links: Option[Links])
  case class BasicEpisode(absoluteNumber: Option[Int],
                          airedEpisodeNumber: Option[Int],
                          airedSeason: Option[Int],
                          dvdEpisodeNumber: Option[Int],
                          dvdSeason: Option[Int],
                          episodeName: Option[String],
                          firstAired: Option[String],
                          id: Option[Int],
                          language: Option[EpisodeLanguageInfo],
                          lastUpdated: Option[Int],
                          overview: Option[String])
  case class JSONErrors(invalidFilters: List[String],
                        invalidLanguage: Option[String],
                        invalidQueryParams: List[String])
  case class Links(first: Option[Int],
                   last: Option[Int],
                   next: Option[Int],
                   previous: Option[Int])
  case class EpisodeLanguageInfo(episodeName: Option[String],
                                 overview: Option[String])

  def seriesEpisodesQuery(id: Int, airedSeason: Int, airedEpisode: Int)(implicit ctx: Context): IO[SeriesEpisodesQuery] = {
    val req = GET(ctx.baseUri / "series" / id.toString / "episodes" / "query"
      withQueryParam("airedSeason", airedSeason)
      withQueryParam("airedEpisode", airedEpisode),
      ctx.acceptHeader,
      ctx.acceptLanguageHeader,
      ctx.authHeader)
    Http1Client[IO]().flatMap { httpClient =>
      httpClient.expect(req)(jsonOf[IO, SeriesEpisodesQuery])
    }
  }
}
