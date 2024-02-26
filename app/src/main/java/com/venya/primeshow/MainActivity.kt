package com.venya.primeshow

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.venya.primeshow.pesentation.viewmodel.MovieListViewModel
import com.venya.primeshow.pesentation.utils.screens.ShowsScreen
import com.venya.primeshow.pesentation.ui.theme.PrimeShowTheme
import com.venya.primeshow.pesentation.utils.screens.DetailsScreen
import com.venya.primeshow.pesentation.viewmodel.MovieDetailsViewModel
import com.venya.primeshow.utils.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrimeShowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController, startDestination = Screen.showsScreen.route
                    ) {
                        composable(route = Screen.showsScreen.route) {
                            val movieListViewModel = hiltViewModel<MovieListViewModel>()
                            ShowsScreen(
                                navController = navController,
                                movieListViewModel = movieListViewModel
                            )
                        }
                        composable(route = Screen.showDetailsScreen.route + "/{movieId}",
                            arguments = listOf(
                                navArgument("movieId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val movieDetailsViewModel = hiltViewModel<MovieDetailsViewModel>()
                            DetailsScreen(navController = navController,applicationContext,movieDetailsViewModel)
                        }
                    }
                }
            }
        }
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}



