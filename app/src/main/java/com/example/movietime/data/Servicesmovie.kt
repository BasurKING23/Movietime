package com.example.movietime.data

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPIService {

    @GET("/")
    suspend fun findMovieById(@Query("i") imdbID: String, @Query("apikey") apiKey: String = "cdfa154c"): Movies

    @GET("/")
    suspend fun searchMoviesByTitle(@Query("s") title: String, @Query("apikey") apiKey: String = "cdfa154c"): MovieSearchResponse
}



