package thetvdb4s

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.circe.{jsonOf, _}
import org.http4s.client.blaze.Http1Client
import org.http4s.client.dsl.io._
import org.http4s.dsl.io.GET

object Episodes {
  case class EpisodeRecordData(data: Episode,
                               errors: Option[JSONErrors])
  case class Episode(absoluteNumber: Int,
                     airedEpisodeNumber: Int,
                     airedSeason: Int,
                     airsAfterSeason: Option[Int],
                     airsBeforeEpisode: Option[Int],
                     airsBeforeSeason: Option[Int],
                     director: String,
                     directors: List[String],
                     dvdChapter: Float,
                     dvdDiscid: String,
                     dvdEpisodeNumber: Float,
                     dvdSeason: Int,
                     episodeName: String,
                     filename: String,
                     firstAired: String,
                     guestStars: List[String],
                     id: Int,
                     imdbId: String,
                     lastUpdated: Int,
                     lastUpdatedBy: Int,
                     overview: String,
                     productionCode: String,
                     seriesId: Int,
                     showUrl: String,
                     siteRating: Float,
                     siteRatingCount: Int,
                     thumbAdded: String,
                     thumbAuthor: Int,
                     thumbHeight: String,
                     thumbWidth: String,
                     writers: List[String])
  case class JSONErrors(invalidFilters: List[String],
                        invalidLanguage: String,
                        invalidQueryParams: List[String])

  def episodes(id: Int)(implicit ctx: Context): IO[EpisodeRecordData] = {
    val req = GET(ctx.baseUri / "episodes" / id.toString, ctx.acceptHeader, ctx.acceptLanguageHeader, ctx.authHeader)
    Http1Client[IO]().flatMap { httpClient =>
      httpClient.expect(req)(jsonOf[IO, EpisodeRecordData])
    }
  }
}
