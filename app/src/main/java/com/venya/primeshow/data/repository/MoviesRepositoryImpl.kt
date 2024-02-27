package com.venya.primeshow.data.repository

import com.venya.primeshow.data.local.FavTvShow
import com.venya.primeshow.data.local.MovieDatabase
import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.data.remote.ApiService
import com.venya.primeshow.domain.repository.MoviesRepository
import com.venya.primeshow.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import retrofit2.http.Query
import javax.inject.Inject

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

class MoviesRepositoryImpl @Inject constructor(private val movieDatabase: MovieDatabase,    private val apiService: ApiService) : MoviesRepository {
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
                emit(handleErrorMessage(e))
                e.printStackTrace()
                return@flow
            }
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
            emit(handleErrorMessage(e))
            e.printStackTrace()
            return@flow
        }
    }

    override suspend fun searchMovies(query: String): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = apiService.searchMovies(query) // Assuming this method exists and returns a list of movies
                if (response.isSuccessful && response.body() != null) {
                    emit(Resource.Success(response.body()!!.results))
                    return@flow
                } else {
                    emit(Resource.Error("An error occurred: ${response.message()}"))
                    return@flow
                }
            } catch (e: Exception) {
                emit(handleErrorMessage(e))
                e.printStackTrace()
                return@flow
            }
        }
    }

    override suspend fun getSimilarMoviesList(movieId: Int): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = apiService.getSimilarMoviesList(movieId) // Assuming this method exists and returns a list of movies
                if (response.isSuccessful && response.body() != null) {
                    emit(Resource.Success(response.body()!!.results))
                    return@flow
                } else {
                    emit(Resource.Error("An error occurred: ${response.message()}"))
                    return@flow
                }
            } catch (e: Exception) {
                emit(handleErrorMessage(e))
                e.printStackTrace()
                return@flow
            }
        }
    }


    private fun <T> handleErrorMessage(e : Exception) : Resource<T>
    {
        return when (e) {
            is IOException -> {
                // This usually indicates a network connectivity issue (e.g., no internet connection)
                Resource.Error("No internet")
            }
            is HttpException -> {
                Resource.Error("Something went wrong")
            }
            else -> {
                Resource.Error("Unknown error, Please try after some time")
            }
        }
    }

    override suspend fun saveFavShow(tvShow: FavTvShow) {
        movieDatabase.getDao().addFavShow(tvShow)
    }

    override suspend fun removeFavShow(id: Int) {
        movieDatabase.getDao().deleteFavShow(id)
    }

    override suspend fun getFavShowList(): Flow<Resource<List<FavTvShow>>>
    {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = movieDatabase.getDao().getFavMovies() // Assuming this method exists and returns a list of movies
                emit(Resource.Success(response))
                return@flow
            } catch (e: Exception) {
                emit(handleErrorMessage(e))
                e.printStackTrace()
                return@flow
            }
        }

    }
}