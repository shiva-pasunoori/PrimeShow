package com.venya.primeshow.data.remote

import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.data.model.response.MovieListRes
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

interface ApiService
{
    @GET("trending/movie/day")
    suspend fun getTrendingMoviesList() : Response<MovieListRes>

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): Response<Movie>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): Response<MovieListRes>
}