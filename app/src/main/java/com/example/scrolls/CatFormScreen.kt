package com.example.scrolls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrolls.data.CatImage
import com.example.scrolls.repository.CatRepository

@Composable
fun CatFormScreen(
    modifier: Modifier = Modifier,
    repository: CatRepository = CatRepository()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var catImages by remember { mutableStateOf<List<CatImage>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var loadTrigger by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Static form section
        Text(
            text = "Cat Form",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                loadTrigger++
                error = null
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Load Cats")
        }

        // Dynamic content section - LazyColumn for loaded images
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        error?.let {
            Text(
                text = "Error: $it",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (catImages.isNotEmpty()) {
            Text(
                text = "Cat Images (${catImages.size})",
                style = MaterialTheme.typography.titleMedium
            )

            LazyColumn(
                modifier = Modifier.height(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(catImages) { catImage ->
                    CatImageItem(catImage = catImage)
                }
            }
        }
    }

    LaunchedEffect(loadTrigger) {
        if (loadTrigger > 0) {
            isLoading = true
            val result = repository.getCatImages(limit = 10)
            result.onSuccess { images ->
                catImages = images
                isLoading = false
            }.onFailure { exception ->
                error = exception.message
                isLoading = false
            }
        }
    }
}

@Composable
fun CatImageItem(
    catImage: CatImage,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = catImage.url,
                contentDescription = stringResource(R.string.cat_image, catImage.id),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(
                        ratio = catImage.width.toFloat() / catImage.height.toFloat(),
                        matchHeightConstraintsFirst = false
                    ),
                contentScale = ContentScale.Crop
            )
        }
    }
}
