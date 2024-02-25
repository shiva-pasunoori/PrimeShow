package com.venya.primeshow.pesentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MovieListViewModel @Inject constructor(private val moviesRepository: MoviesRepository) : ViewModel()
{
    // Initialize _moviesList with an empty Resource.Loading state as the initial state
    private var _moviesList = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading())
    val moviesList: StateFlow<Resource<List<Movie>>> = _moviesList.asStateFlow()

    private var _stateSearchActive = MutableStateFlow(false)
    val stateSearchActive = _stateSearchActive.asStateFlow()

    private var _stateSearchText = MutableStateFlow("")
    val stateSearchText = _stateSearchText.asStateFlow()

    init {
        fetchTrendingMovies()
    }

    fun fetchTrendingMovies(query: String = "") {
        viewModelScope.launch {
            moviesRepository.getTrendingMoviesList().collect { moviesResource ->
                _moviesList.value = moviesResource
            }
        }
    }
    fun onActiveChange(active: Boolean) {
        _stateSearchActive.value = active
    }

    fun onQueryChange(query: String) {
        _stateSearchText.value = query
    }

    fun onSearch(query: String)  {
        fetchTrendingMovies(query)
    }

    fun clearSearch() {
        _stateSearchText.value = ""
    }

    fun closeSearch() {
        _stateSearchText.value = ""
        // _moviesList.value = DataStateWrapper.Idle()
        _stateSearchActive.value = false
    }
}