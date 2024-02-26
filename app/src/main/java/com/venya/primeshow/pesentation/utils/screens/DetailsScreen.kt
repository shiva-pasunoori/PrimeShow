package com.venya.primeshow.pesentation.utils.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.venya.primeshow.R
import com.venya.primeshow.data.model.response.Movie
import com.venya.primeshow.pesentation.utils.common.MovieCard
import com.venya.primeshow.pesentation.utils.common.RatingBar
import com.venya.primeshow.pesentation.viewmodel.MovieDetailsViewModel
import com.venya.primeshow.utils.Constants
import com.venya.primeshow.utils.Resource
import java.util.Locale

/**
 * Created by Shiva Pasunoori on 25,February,2024
 */

@Composable
fun DetailsScreen(
    navController: NavHostController,
    applicationContext: Context,
    movieDetailsViewModel: MovieDetailsViewModel,
) {


    val moviesDetailsState by movieDetailsViewModel.movieDetails.collectAsState()

    val similarMoviesDetailsState by movieDetailsViewModel.similarMoviesList.collectAsState()

    when (moviesDetailsState) {
        is Resource.Loading -> LoadingView()
        is Resource.Success -> moviesDetailsState.data?.let {
            detailsScreenBody(it,similarMoviesDetailsState,navController)
        }

        is Resource.Error -> ErrorView(
            message = moviesDetailsState.message ?: "An unknown error occurred"
        )

        else -> {}
    }
}

@Composable
fun detailsScreenBody(
    movie: Movie,
    similarMoviesDetailsState: Resource<List<Movie>>,
    navController: NavHostController
) {
    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Constants.IMAGE_BASE_URL + movie.backdropPath)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Constants.IMAGE_BASE_URL + movie.posterPath)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {

        if (backDropImageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = movie.title
                )
            }
        }

        if (backDropImageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                painter = backDropImageState.painter,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(1.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(240.dp)
            ) {
                if (posterImageState is AsyncImagePainter.State.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = movie.title
                        )
                    }
                }

                if (posterImageState is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        painter = posterImageState.painter,
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            movie.let { movie ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    movie.title?.let {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = it,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp)
                    ) {
                        RatingBar(
                            starsModifier = Modifier.size(18.dp),
                            rating = movie.voteAverage?.div(2) ?: 0.0,
                            starsColor = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = movie.voteAverage.toString().take(3),
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            maxLines = 1,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.language) + " " + (movie.originalLanguage?.uppercase(
                            Locale.ROOT
                        ) ?: "")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.release_date) + " " + movie.releaseDate
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = movie.voteCount.toString() + " " + stringResource(R.string.votes)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(1.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.overview),
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        movie.let {
            it.overview?.let { it1 ->
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = it1,
                    fontSize = 16.sp,
                )
            }
        }

        // Spacer(modifier = Modifier.height(8.dp))

        if (similarMoviesDetailsState is Resource.Success) {

            Text(
                text = stringResource(id = R.string.similar_shows),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 19.sp,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 26.dp),
                textAlign = TextAlign.Start,
            )

            // Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSecondary),

                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                content = {
                    similarMoviesDetailsState.data?.size?.let {
                        items(it) { index ->
                            MovieCard(
                                movie = similarMoviesDetailsState.data!![index],
                                navController
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun similarMoviesList(movies: List<Movie>, navController: NavHostController) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSecondary),

        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp),
        content = {
            items(movies.size) { index ->
                MovieCard(movie = movies[index], navController)
            }
        }
    )
}
