package com.venya.primeshow.pesentation.utils.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.pesentation.utils.common.MovieCard
import com.venya.primeshow.pesentation.utils.common.MyErrorMessage
import com.venya.primeshow.pesentation.utils.common.MyProgressBar
import com.venya.primeshow.pesentation.utils.common.NoInternetConnectionMessage
import com.venya.primeshow.pesentation.utils.common.RoundedSearchWidgetWithClear
import com.venya.primeshow.pesentation.viewmodel.MovieListViewModel
import com.venya.primeshow.utils.Resource
import com.venya.primeshow.utils.checkIfHasNetwork

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

@Composable
fun ShowsScreen(navController: NavController, movieListViewModel: MovieListViewModel) {
    val stateSearchText = movieListViewModel.stateSearchText.collectAsState().value
    val stateSearchActive = movieListViewModel.stateSearchActive.collectAsState().value

    val searchHistory = remember { mutableStateListOf<String>() }


    val moviesResource by movieListViewModel.moviesList.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {

        MoviesListView(
            navController,
            moviesResource = moviesResource,
            movieListViewModel
        )
//        when (moviesResource) {
//            is Resource.Loading -> LoadingView()
//            is Resource.Success -> moviesResource.data?.let {
//                MoviesListView(
//                    navController,
//                    movies = it,
//                    movieListViewModel
//                )
//            }
//
//            is Resource.Error -> ErrorView(
//                message = moviesResource.message ?: "An unknown error occurred"
//            )
//        }
    }
}

@Composable
fun LoadingView() {
    MyProgressBar()
}

@Composable
fun MoviesListView(
    navController: NavController,
    moviesResource: Resource<List<Movie>>,
    movieListViewModel: MovieListViewModel
) {

    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val hasNetwork = remember { mutableStateOf(false) }
    val showNetworkErrorMessage = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        hasNetwork.value = context.checkIfHasNetwork()
    }

    Column {
        RoundedSearchWidgetWithClear(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 20.dp),
            hint = "Search TV Shows...",
            onValueChange = { query ->
                if (hasNetwork.value)
                {
                    if(query.isNotBlank()) {
                        movieListViewModel.searchMovies(query)
                    }
                    else{
                        movieListViewModel.fetchTrendingMovies()
                    }
                    showNetworkErrorMessage.value = false
                }
                else{
                    showNetworkErrorMessage.value = true
                }
            }
        )

        if (showNetworkErrorMessage.value) {
            checkYourInterNet()
        }

        if (moviesResource is Resource.Loading) {
            MyProgressBar()
        } else if (moviesResource is Resource.Success) {
            if (moviesResource.data?.size!! > 0) {
                val movies: List<Movie> = moviesResource.data
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onSecondary),

                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp),
                    content = {
                        items(movies.size) { index ->
                            MovieCard(movie = movies.get(index), navController)
                        }
                    }
                )
            } else {
                ErrorView(message = "No TV Shows Found")
            }
        } else if (moviesResource is Resource.Error)
        {
            if(moviesResource.message.equals("No internet"))
            {
                checkYourInterNet()
            }
            else {
                ErrorView(
                    message = moviesResource.message ?: "An unknown error occurred"
                )
            }
        }
    }
}

@Composable
fun ErrorView(message: String) {
    MyErrorMessage(message = message)
}

@Composable
fun checkYourInterNet() {
    NoInternetConnectionMessage()
}