package com.venya.primeshow.pesentation.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.venya.primeshow.data.local.FavTvShow
import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.pesentation.components.ExitDialog
import com.venya.primeshow.pesentation.components.MovieCard
import com.venya.primeshow.pesentation.components.MyErrorMessage
import com.venya.primeshow.pesentation.components.MyProgressBar
import com.venya.primeshow.pesentation.components.NoInternetConnectionMessage
import com.venya.primeshow.pesentation.components.RoundedSearchWidgetWithClear
import com.venya.primeshow.pesentation.viewmodel.MovieListViewModel
import com.venya.primeshow.utils.Resource
import com.venya.primeshow.utils.Screen
import com.venya.primeshow.utils.checkIfHasNetwork

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

@Composable
fun MoviesListScreen(navController: NavController, movieListViewModel: MovieListViewModel) {

    val showExitDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current as ComponentActivity

    BackHandler(onBack = {
        showExitDialog.value = true
    })

    if (showExitDialog.value) {
        ExitDialog(title = "Exit App", message = "Are you sure you want to exit?", onConfirm = {
            // Handle exit confirmation
            context.finish()
        }, onDismiss = {
            showExitDialog.value = false
        })
    }

    val moviesResource by movieListViewModel.moviesList.collectAsState()
    val favMoviesResource by movieListViewModel.favMoviesList.collectAsState()

    val listener = remember {
        NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.route.toString() == Screen.showsScreen.route) {
                movieListViewModel.fetchFavMovies()
            }
        }
    }
    DisposableEffect(Unit) {
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    MoviesListViewContent(
        navController, moviesResource = moviesResource, movieListViewModel, favMoviesResource
    )
}

@Composable
fun LoadingView() {
    MyProgressBar()
}

@Composable
fun MoviesListViewContent(
    navController: NavController,
    moviesResource: Resource<List<Movie>>,
    movieListViewModel: MovieListViewModel,
    favMoviesResource: Resource<List<FavTvShow>>
) {

    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val hasNetwork = remember { mutableStateOf(false) }
    val showNetworkErrorMessage = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        hasNetwork.value = context.checkIfHasNetwork()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {

        Column {
            RoundedSearchWidgetWithClear(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 20.dp),
                hint = "Search TV Shows...",
                onValueChange = { query ->
                    if (hasNetwork.value) {
                        if (query.isNotBlank()) {
                            movieListViewModel.searchMovies(query)
                        } else {
                            movieListViewModel.fetchTrendingMovies()
                        }
                        showNetworkErrorMessage.value = false
                    } else {
                        showNetworkErrorMessage.value = true
                    }
                })

            if (showNetworkErrorMessage.value) {
                checkYourInterNet()
            }
        }

        // Depending on the state of moviesResource, display the appropriate view
        when (moviesResource) {
            is Resource.Loading -> LoadingView()
            is Resource.Success -> MoviesListView(
                navController = navController,
                movies = moviesResource.data ?: emptyList(),
                favMovies = favMoviesResource.data ?: emptyList(),
                movieListViewModel = movieListViewModel
            )

            is Resource.Error -> ErrorView(
                message = moviesResource.message ?: "An unknown error occurred"
            )
        }
    }
}

@Composable
fun MoviesListView(
    navController: NavController,
    movies: List<Movie>,
    favMovies: List<FavTvShow>,
    movieListViewModel: MovieListViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
    ) {
        items(movies.size) { index ->
            var isFav = false
            val movie = movies[index]
            if (favMovies.let {
                    isMovieAFavorite(
                        movie, it
                    )
                }) {
                isFav = true
            }
            MovieCard(
                movie = movies[index],
                isFav = isFav,
                navController = navController
            )
        }
    }
}

fun isMovieAFavorite(movie: Movie, favTvShows: List<FavTvShow>): Boolean {
    // Assuming movie.id and FavTvShow.id are the common identifiers
    // and are sufficient for determining uniqueness.
    return favTvShows.any { favTvShow ->
        movie.id == favTvShow.id
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