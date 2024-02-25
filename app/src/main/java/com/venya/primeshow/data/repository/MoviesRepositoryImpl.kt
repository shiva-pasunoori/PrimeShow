package com.venya.primeshow.data.repository

import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.data.remote.ApiService
import com.venya.primeshow.domain.repository.MoviesRepository
import com.venya.primeshow.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

class MoviesRepositoryImpl @Inject constructor(private val apiService: ApiService) : MoviesRepository {
    override suspend fun getTrendingMoviesList(): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = apiService.getTrendingMoviesList() // Assuming this method exists and returns a list of movies
                if (response.isSuccessful && response.body() != null) {
                    emit(Resource.Success(response.body()!!.results))
                    return@flow
                } else {
                    emit(Resource.Error("An error occurred: ${response.message()}"))
                    return@flow
                }
            } catch (e: Exception) {
                emit(Resource.Error("An error occurred: ${e.message}"))
                e.printStackTrace()
                return@flow
            }
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovieDetails(id: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading(true))
        try {
            val response = apiService.getMovieDetails(id) // Assuming this method exists and returns details for a specific movie
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
                return@flow
            } else {
                emit(Resource.Error("An error occurred: ${response.message()}"))
                return@flow
            }
        } catch (e: Exception) {
            emit(Resource.Error("An error occurred: ${e.message}"))
            e.printStackTrace()
            return@flow
        }
        emit(Resource.Loading(false))
    }
}