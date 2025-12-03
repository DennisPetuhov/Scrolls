package com.example.scrolls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import com.example.scrolls.R

data class Category(
    val id: String,
    val title: String,
    val items: List<Item>
)

data class Item(
    val id: String,
    val name: String,
    val color: Color = Color.Unspecified
)

@Composable
fun NestedScrollExample(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    columnState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = columnState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        items(
            items = categories,
            key = { category -> category.id }
        ) { category ->
            CategoryCard(category = category)
        }
    }
}

@Composable
fun CategoryCard(
    category: Category,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius_large)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation_medium))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.text_bottom_padding))
            )

            CategoryItemsRow(
                items = category.items,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CategoryItemsRow(
    items: List<Item>,
    modifier: Modifier = Modifier,
    rowState: LazyListState = rememberLazyListState()
) {
    LazyRow(
        state = rowState,
        modifier = modifier.height(dimensionResource(R.dimen.category_row_height)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_tiny))
    ) {
        items(
            items = items,
            key = { item -> item.id }
        ) { item ->
            ItemCard(item = item)
        }
    }
}

@Composable
fun ItemCard(
    item: Item,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(dimensionResource(R.dimen.item_card_size))
            .height(dimensionResource(R.dimen.item_card_size)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius_medium)),
        colors = CardDefaults.cardColors(
            containerColor = item.color.takeIf { it != Color.Unspecified }
                ?: MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation_small))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_small)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Composable
fun stringResource(@StringRes id: Int): String {
    return LocalContext.current.getString(id)
}

@Composable
fun stringResource(@StringRes id: Int, vararg formatArgs: Any): String {
    return LocalContext.current.getString(id, *formatArgs)
}

@Composable
fun dimensionResource(@DimenRes id: Int): Dp {
    val resources = LocalContext.current.resources
    return (resources.getDimension(id) / resources.displayMetrics.density).dp
}

@Composable
fun sampleCategories(): List<Category> {
    return listOf(
        Category(
            id = "1",
            title = stringResource(R.string.popular_movies),
            items = listOf(
                Item(id = "1-1", name = stringResource(R.string.movie, 1), color = Color(0xFF6200EE)),
                Item(id = "1-2", name = stringResource(R.string.movie, 2), color = Color(0xFF03DAC6)),
                Item(id = "1-3", name = stringResource(R.string.movie, 3), color = Color(0xFF018786)),
                Item(id = "1-4", name = stringResource(R.string.movie, 4), color = Color(0xFF6200EE)),
                Item(id = "1-5", name = stringResource(R.string.movie, 5), color = Color(0xFF03DAC6)),
                Item(id = "1-6", name = stringResource(R.string.movie, 6), color = Color(0xFF018786))
            )
        ),
        Category(
            id = "2",
            title = stringResource(R.string.tv_shows),
            items = listOf(
                Item(id = "2-1", name = stringResource(R.string.show, 1), color = Color(0xFFFF6B6B)),
                Item(id = "2-2", name = stringResource(R.string.show, 2), color = Color(0xFF4ECDC4)),
                Item(id = "2-3", name = stringResource(R.string.show, 3), color = Color(0xFF45B7D1)),
                Item(id = "2-4", name = stringResource(R.string.show, 4), color = Color(0xFFFF6B6B)),
                Item(id = "2-5", name = stringResource(R.string.show, 5), color = Color(0xFF4ECDC4))
            )
        ),
        Category(
            id = "3",
            title = stringResource(R.string.documentaries),
            items = listOf(
                Item(id = "3-1", name = stringResource(R.string.doc, 1), color = Color(0xFF96CEB4)),
                Item(id = "3-2", name = stringResource(R.string.doc, 2), color = Color(0xFFFECA57)),
                Item(id = "3-3", name = stringResource(R.string.doc, 3), color = Color(0xFFFF9FF3)),
                Item(id = "3-4", name = stringResource(R.string.doc, 4), color = Color(0xFF96CEB4)),
                Item(id = "3-5", name = stringResource(R.string.doc, 5), color = Color(0xFFFECA57)),
                Item(id = "3-6", name = stringResource(R.string.doc, 6), color = Color(0xFFFF9FF3)),
                Item(id = "3-7", name = stringResource(R.string.doc, 7), color = Color(0xFF96CEB4))
            )
        ),
        Category(
            id = "4",
            title = stringResource(R.string.anime),
            items = listOf(
                Item(id = "4-1", name = stringResource(R.string.anime_item, 1), color = Color(0xFFA8E6CF)),
                Item(id = "4-2", name = stringResource(R.string.anime_item, 2), color = Color(0xFFDDA0DD)),
                Item(id = "4-3", name = stringResource(R.string.anime_item, 3), color = Color(0xFF98D8C8)),
                Item(id = "4-4", name = stringResource(R.string.anime_item, 4), color = Color(0xFFA8E6CF))
            )
        ),
        Category(
            id = "5",
            title = stringResource(R.string.comedy_specials),
            items = listOf(
                Item(id = "5-1", name = stringResource(R.string.comedy, 1), color = Color(0xFFFFD93D)),
                Item(id = "5-2", name = stringResource(R.string.comedy, 2), color = Color(0xFF6BCB77)),
                Item(id = "5-3", name = stringResource(R.string.comedy, 3), color = Color(0xFFFFD93D)),
                Item(id = "5-4", name = stringResource(R.string.comedy, 4), color = Color(0xFF6BCB77)),
                Item(id = "5-5", name = stringResource(R.string.comedy, 5), color = Color(0xFFFFD93D))
            )
        )
    )
}


