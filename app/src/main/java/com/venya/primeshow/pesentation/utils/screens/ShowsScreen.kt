package com.venya.primeshow.pesentation.utils.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.venya.primeshow.R
import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.pesentation.utils.common.MovieCard
import com.venya.primeshow.pesentation.viewmodel.MovieListViewModel
import com.venya.primeshow.pesentation.utils.common.MyErrorMessage
import com.venya.primeshow.pesentation.utils.common.MyProgressBar
import com.venya.primeshow.utils.Resource

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowsScreen(navController: NavController, movieListViewModel: MovieListViewModel) {
    val stateSearchText = movieListViewModel.stateSearchText.collectAsState().value
    val stateSearchActive = movieListViewModel.stateSearchActive.collectAsState().value

    val searchHistory = remember { mutableStateListOf<String>() }

    val moviesResource by movieListViewModel.moviesList.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    IconButton(
                        onClick = { movieListViewModel.onActiveChange(true) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(id = R.string.search_hint)
                        )
                    }
                }
            )
        },
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) { paddingValues ->
        if (stateSearchActive) {
            SearchBar(
                query = stateSearchText,
                onQueryChange = {
                    movieListViewModel.onQueryChange(it)
                },
                onSearch = {
                    searchHistory.add(it)
                    movieListViewModel.onSearch(it)
                },
                active = true,
                onActiveChange = {
                    movieListViewModel.onActiveChange(it)
                },
                placeholder = {
                    Text(text = stringResource(id = R.string.search_hint))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search_hint)
                    )
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            if (stateSearchText.isNotEmpty()) {
                                movieListViewModel.clearSearch()
                            } else {
                                movieListViewModel.closeSearch()
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.search_close)
                    )
                }
            ) {
                // previously searched history
                searchHistory.forEach {
                    Row(modifier = Modifier.padding(all = 14.dp)) {
                        Icon(
                            modifier = Modifier.padding(end = 10.dp),
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(id = R.string.history)
                        )
                        Text(text = it)
                    }
                }
            }
        }


        when (moviesResource) {
            is Resource.Loading -> LoadingView()
            is Resource.Success -> moviesResource.data?.let {
                MoviesListView(
                    navController,
                    movies = it,
                    paddingValues
                )
            }

            is Resource.Error -> ErrorView(
                message = moviesResource.message ?: "An unknown error occurred"
            )
        }
    }
}

@Composable
fun LoadingView() {
    MyProgressBar()
}

@Composable
fun MoviesListView(
    navController: NavController,
    movies: List<Movie>,
    paddingValues: PaddingValues
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSecondary)
            .padding(top = 60.dp),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp),
        content = {
            items(movies.size) { index ->
                MovieCard(movie = movies.get(index),navController)
            }
        }
    )
}

@Composable
fun ErrorView(message: String) {
    MyErrorMessage(message = message)

}