package thetvdb4s

import org.http4s.{AuthScheme, Credentials, LanguageTag, MediaType, Uri}
import org.http4s.headers.{`Accept-Language`, Accept, Authorization}
import thetvdb4s.Authentication.Token

case class Context private(baseUri: Uri,
                           acceptHeader: Accept,
                           acceptLanguageHeader: `Accept-Language`,
                           private val authHeaderOption: Option[Authorization]) {
  def updateAuthHeather(token: Token): Context = {
    copy(authHeaderOption = Some(Authorization(Credentials.Token(AuthScheme.Bearer, token.token))))
  }

  def authHeader: Authorization = {
    require(authHeaderOption.isDefined, "Context requires a valid authorization header")
    authHeaderOption.get
  }
}

object Context {
  private val DefaultBaseUri = "https://api.thetvdb.com/"
  private val DefaultVersion = "2.1.2"
  private val DefaultLanguage = "en"
  private val MainType = "application"
  private val SubType = "vnd.thetvdb.v%s"
  private val Compressible = true
  private val Binary = true

  def apply(baseUri: String = DefaultBaseUri, version: String = DefaultVersion, language: String = DefaultLanguage): Context = {
    val uri = Uri.fromString(baseUri) match {
      case Left(ex) => throw ex
      case Right(uri) => uri
    }
    val accept = Accept(new MediaType(MainType, SubType.format(version), Compressible, Binary))
    val acceptLanguage = `Accept-Language`(LanguageTag(language))
    new Context(uri, accept, acceptLanguage, None)
  }
}


