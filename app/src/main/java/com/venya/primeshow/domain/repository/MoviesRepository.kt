package com.venya.primeshow.domain.repository

import com.venya.primeshow.data.local.FavTvShow
import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

interface MoviesRepository {
    suspend fun getTrendingMoviesList() : Flow<Resource<List<Movie>>>

    suspend fun getMovieDetails(id : Int) : Flow<Resource<Movie>>

    suspend fun searchMovies(query: String): Flow<Resource<List<Movie>>>

    suspend fun getSimilarMoviesList(movieId : Int): Flow<Resource<List<Movie>>>

    suspend fun saveFavShow(tvShow: FavTvShow)

    suspend fun removeFavShow(id: Int)

    suspend fun getFavShowList(): Flow<Resource<List<FavTvShow>>>
}