package com.venya.primeshow.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.venya.primeshow.data.local.FavTvShow
import com.venya.primeshow.data.model.response.Movie

/**
 * Created by Shiva Pasunoori on 26,February,2024
 */

fun Context?.checkIfHasNetwork(): Boolean {
    val cm = this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
    return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}


fun movieToFavTvShow(movie: Movie): FavTvShow {
    // Determine the appropriate title/name for the TV show
    val name = movie.name ?: movie.originalTitle ?: "Unknown Title"

    // Convert the vote average to a string, handling nulls
    val voteAverage = movie.voteAverage?.toString() ?: "0.0"

    // Handle the poster path, assuming it might need a base URL
    // Example: "https://image.tmdb.org/t/p/w500/"
    val posterPath = movie.posterPath?.let { Constants.IMAGE_BASE_URL + it } ?: ""

    return FavTvShow(
        id = movie.id ?: 0, // Handle potential null ID
        name = name,
        posterPath = posterPath,
        voteAverage = voteAverage
    )
}

fun isMovieAFavorite(movie: Movie, favTvShows: List<FavTvShow>): Boolean {
    // Assuming movie.id and FavTvShow.id are the common identifiers
    // and are sufficient for determining uniqueness.
    return favTvShows.any { favTvShow ->
        movie.id == favTvShow.id
    }
}