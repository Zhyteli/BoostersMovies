package com.boosterstestmovis.data.api

import com.boosterstestmovis.data.pojo.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie")
    suspend fun getMovieResponse(
        @Query("language") language: String,
        @Query("sort_by") sort: String,
        @Query("vote_count.gte") minVoteCountValue: String,
        @Query("vote_average.gte") minVoteAverageValue: String = "7",
        @Query("page") page: String
    ): Response<MovieResponse>
}