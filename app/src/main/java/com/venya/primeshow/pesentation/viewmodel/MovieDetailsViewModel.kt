package com.venya.primeshow.pesentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venya.primeshow.data.local.FavTvShow
import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.domain.repository.MoviesRepository
import com.venya.primeshow.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    // Initialize _moviesList with an empty Resource.Loading state as the initial state
    private var _movieDetails = MutableStateFlow<Resource<Movie>>(Resource.Loading())
    val movieDetails: StateFlow<Resource<Movie>> = _movieDetails.asStateFlow()

    private val movieId = savedStateHandle.get<Int>("movieId")

    private var _similarMoviesList = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading())
    val similarMoviesList: StateFlow<Resource<List<Movie>>> = _similarMoviesList.asStateFlow()

    init {
        fetchMovieDetails(movieId = movieId ?: -1)
        similarMoviesList(movieId = movieId ?: -1)
    }

    private fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            moviesRepository.getMovieDetails(movieId).collect { moviesResource ->
                _movieDetails.value = moviesResource
            }
        }
    }

    fun similarMoviesList(movieId: Int) {
        viewModelScope.launch {
            moviesRepository.getSimilarMoviesList(movieId).collect { moviesResource ->
                _similarMoviesList.value = moviesResource
            }
        }
    }

    fun saveFavShow(favTvShow: FavTvShow)
    {
        viewModelScope.launch {
            moviesRepository.saveFavShow(favTvShow)
        }
    }
    fun deleteFavShow(id: Int)
    {
        viewModelScope.launch {
            moviesRepository.removeFavShow(id)
        }
    }

}