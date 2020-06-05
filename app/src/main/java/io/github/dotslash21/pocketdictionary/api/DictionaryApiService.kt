package io.github.dotslash21.pocketdictionary.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.dotslash21.pocketdictionary.SkipBadListObjectsAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Dictionary API public API base url
private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(SkipBadListObjectsAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object pointing to the desired URL
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getDefinitions] method
 */
interface DictionaryApiService {
    /**
     * Returns a Retrofit callback that delivers a String
     * The @GET annotation indicates that the "/{searchWord}" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("{searchWord}")
    fun getDefinitions(@Path("searchWord") word: String): Deferred<List<WordDefinition>>
}

/**
 * Singleton class for the Dictionary API service
 */
object DictionaryApi {
    val retrofitService: DictionaryApiService by lazy {
        retrofit.create(DictionaryApiService::class.java)
    }
}