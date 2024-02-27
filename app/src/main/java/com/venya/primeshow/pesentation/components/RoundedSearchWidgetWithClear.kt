package com.venya.primeshow.pesentation.components

/**
 * Created by Shiva Pasunoori on 26,February,2024
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedSearchWidgetWithClear(
    modifier: Modifier = Modifier,
    hint: String = "Search...",
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit = {}
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    val customColors = TextFieldDefaults.colors(
        disabledTextColor = Color.Gray, // Text color when disabled
        focusedContainerColor = Color.White, // Background color; not directly applicable for OutlinedTextField
        disabledContainerColor = Color.White, // Background color when disabled; for OutlinedTextField, consider background modifier
        unfocusedContainerColor = Color.White, // Border color when unfocused
        // Add other customizations as needed
    )

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it.text)
        },
        placeholder = { Text(hint) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search, // Replace with your search icon resource
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (text.text.isNotEmpty()) {
                IconButton(onClick = {
                    text = TextFieldValue("") // Clear text
                    onValueChange("")
                    // focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Clear, // Replace with your clear icon resource
                        contentDescription = "Clear Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50.dp), // Adjust the corner size to suit your design
        colors = customColors,
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(50.dp)
            )
            .focusRequester(focusRequester)
            .padding(2.dp)
    )
}

@Preview(showBackground = false)
@Composable
fun PreviewRoundedSearchWidgetWithClear() {
    MaterialTheme {
        RoundedSearchWidgetWithClear(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onValueChange = {
                // Handle text change, e.g., filter the list based on the query
            }
        )
    }
}