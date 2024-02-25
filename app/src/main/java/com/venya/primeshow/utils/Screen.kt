package com.venya.primeshow.utils

import androidx.compose.runtime.Composable

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

sealed class Screen(val route: String) {
    data object showsScreen: Screen("tv_shows_screen")
    data object showDetailsScreen: Screen("show_details_screen")
}