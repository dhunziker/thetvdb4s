# thetvdb4s

## Quickstart

Create an implicit Context:

```scala
val auth = Auth("<APIKEY>", "<USERKEY>", "<USERNAME>")
implicit val ctx = Authentication.login(auth).unsafeRunSync
```

Override version and language settings:

```scala
val auth = Auth("<APIKEY>", "<USERKEY>", "<USERNAME>")
val seed = Context(version = "1", language = "de")
implicit val ctx = Authentication.login(auth, seed).unsafeRunSync
```

Search for a TV series by name:

```scala
val series = Search.searchSeries("the blacklist").unsafeRunSync
series.data.map(_.seriesName).foreach(println)
```
