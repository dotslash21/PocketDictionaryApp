package io.github.dotslash21.pocketdictionary.api

import com.squareup.moshi.Json

data class Definition(
    @Json(name = "definition") val definition: String,
    @Json(name = "example") val example: String = "",
    @Json(name = "synonyms") val synonyms: List<String> = ArrayList()
)

data class Meaning(
    @Json(name = "partOfSpeech") val partOfSpeech: String,
    @Json(name = "definitions") val definitions: List<Definition>
)

data class WordDefinition(
    @Json(name = "word") val word: String,
    @Json(name = "phonetic") val phonetic: String = "",
    @Json(name = "origin") val origin: String = "",
    @Json(name = "meanings") val meanings: List<Meaning> = ArrayList()
)