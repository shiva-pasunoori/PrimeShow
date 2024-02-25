package com.venya.primeshow.domain.repository

import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

interface MoviesRepository {
    suspend fun getTrendingMoviesList() : Flow<Resource<List<Movie>>>

    suspend fun getMovieDetails(id : Int) : Flow<Resource<Movie>>
}