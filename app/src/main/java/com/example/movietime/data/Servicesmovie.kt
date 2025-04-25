package com.example.movietime.data

import android.graphics.Movie
import android.telecom.Call


interface OmdbApiService { @GET(".")
    suspend fun getMovieByTitle(@Query("cdfa154c") apiKey: String, @Query("s") title: String): Call<Movies>

    @GET(".")
    suspend fun getMovieById(@Query("cdfa154c") apiKey: String, @Query("i") imdbId: String): Call<Movies>

    annotation class GET(val s: String)

    annotation class Query(val s: String)
}

