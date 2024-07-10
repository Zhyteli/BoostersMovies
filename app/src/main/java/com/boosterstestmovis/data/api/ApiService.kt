package com.boosterstestmovis.data.api

import com.boosterstestmovis.data.pojo.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie")
    suspend fun getMovieResponse(
        @Query(ApiParams.LANGUAGE) language: String,
        @Query(ApiParams.SORT_BY) sort: String,
        @Query(ApiParams.VOTE_COUNT_GTE) minVoteCountValue: String,
        @Query(ApiParams.VOTE_AVERAGE_GTE) minVoteAverageValue: String = "7",
        @Query(ApiParams.PAGE) page: String
    ): Response<MovieResponse>
}