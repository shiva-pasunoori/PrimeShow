package com.venya.primeshow.pesentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.domain.repository.MoviesRepository
import com.venya.primeshow.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

@OptIn(FlowPreview::class)
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

    private val _searchQuery = MutableStateFlow("")

    init {
        fetchTrendingMovies()
        viewModelScope.launch {
            _searchQuery
                .filter { query -> query.length >= 3 }
                .debounce(500)
                .collectLatest { query ->
                    searchMovies(query)
                }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun fetchTrendingMovies() {
        viewModelScope.launch {
            moviesRepository.getTrendingMoviesList().collect { moviesResource ->
                _moviesList.value = moviesResource
            }
        }
    }

    fun searchMovies(query: String)
    {
        viewModelScope.launch {
            moviesRepository.searchMovies(query).collect { moviesResource ->
                _moviesList.value = moviesResource
            }
        }
    }
}