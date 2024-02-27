package com.venya.primeshow.pesentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.pesentation.screens.movieToFavTvShow
import com.venya.primeshow.pesentation.viewmodel.MovieDetailsViewModel

/**
 * Created by Shiva Pasunoori on 27,February,2024
 */

@Composable
fun FavoriteIconButton(movie: Movie, movieDetailsViewModel: MovieDetailsViewModel) {
    // Remember a mutable state for isFav
    var isFav by remember { mutableStateOf(false) }

    // Determine the icon and color based on isFav
    val icon = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
    val favColor = if (isFav) MaterialTheme.colorScheme.primary else Color.White // Adjust colors as needed

    IconButton(onClick = {
        // Toggle the favorite status
        isFav = !isFav

        // Perform the corresponding action based on the new value of isFav
        if (isFav) {
            val favTvShow = movieToFavTvShow(movie = movie)
            movieDetailsViewModel.saveFavShow(favTvShow)
        } else {
            movieDetailsViewModel.deleteFavShow(movie.id!!)
        }
    }) {
        Icon(
            modifier = Modifier.padding(start = 16.dp),
            imageVector = icon,
            contentDescription = "Favorite Movie",
            tint = favColor
        )
    }
}