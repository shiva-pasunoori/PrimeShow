package com.venya.primeshow.pesentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Created by Shiva Pasunoori on 27,February,2024
 */

@Composable
fun CustomBackButton(navController: NavController) {
    IconButton(modifier = Modifier.padding(top = 12.dp), onClick = { navController.navigateUp() }) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
    }
}
