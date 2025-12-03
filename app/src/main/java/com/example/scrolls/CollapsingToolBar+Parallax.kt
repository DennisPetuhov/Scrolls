@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.scrolls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.example.scrolls.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
//https://www.droidcon.com/2022/10/10/collapsing-toolbar-with-parallax-effect-and-curved-motion-in-jetpack-compose-%F0%9F%98%8E/
private const val titleFontScaleStart = 1f
private const val titleFontScaleEnd = 0.66f
private const val screenDismissThreshold = 100f

data class ListItem(
    val id: String,
    val title: String,
    val content: String
)

@Composable
private fun getSampleData(loremText: String): List<ListItem> = listOf(
    ListItem("1", stringResource(R.string.article_one), loremText),
    ListItem("2", stringResource(R.string.article_two), loremText),
    ListItem("3", stringResource(R.string.article_three), loremText),
    ListItem("4", stringResource(R.string.article_four), loremText),
    ListItem("5", stringResource(R.string.article_five), loremText),
    ListItem("6", stringResource(R.string.article_six), loremText),
    ListItem("7", stringResource(R.string.article_seven), loremText),
    ListItem("8", stringResource(R.string.article_eight), loremText),
    ListItem("9", stringResource(R.string.article_nine), loremText),
    ListItem("10", stringResource(R.string.article_ten), loremText),
    ListItem("11", stringResource(R.string.article_eleven), loremText),
    ListItem("12", stringResource(R.string.article_twelve), loremText),
    ListItem("13", stringResource(R.string.article_thirteen), loremText),
    ListItem("14", stringResource(R.string.article_fourteen), loremText),
    ListItem("15", stringResource(R.string.article_fifteen), loremText)
)

@Preview
@Composable
fun CollapsingToolBarParallax(
    modifier: Modifier = Modifier,
    onDismissScreen: (() -> Unit)? = null
) {
    val density = LocalDensity.current
    val headerHeight = dimensionResource(R.dimen.header_height)
    val toolbarHeight = dimensionResource(R.dimen.toolbar_height)
    val headerHeightPx = density.run { headerHeight.toPx() }
    val toolbarHeightPx = density.run { toolbarHeight.toPx() }
    val listState = rememberLazyListState()
    val loremText = stringResource(R.string.lorem_ipsum)
    val sampleData = getSampleData(loremText)
    val items = remember { mutableStateListOf(*sampleData.toTypedArray()) }
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    val onRefresh: () -> Unit = {
        isRefreshing = true
        coroutineScope.launch {
            delay(1500)
            isRefreshing = false
        }
    }
    
    val pullRefreshState = rememberPullToRefreshState()
    
    val scrollPosition = remember {
        derivedStateOf {
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset
            if (firstVisibleItemIndex == 0) {
                firstVisibleItemScrollOffset.toFloat()
            } else {
                headerHeightPx + (firstVisibleItemIndex * 200f) + firstVisibleItemScrollOffset.toFloat()
            }
        }
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        Header(headerHeightPx, scrollPosition.value, headerHeight)
        PullToRefreshBox(
            state = pullRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    state = pullRefreshState,
                    isRefreshing = isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            Body(
                listState = listState,
                items = items,
                onDismissScreen = onDismissScreen,
                headerHeight = headerHeight
            )
        }
        Toolbar(scrollPosition.value, headerHeightPx, toolbarHeightPx)
        Title(scrollPosition.value, headerHeightPx, toolbarHeightPx)
    }
}

@Composable
private fun Title(
    scrollPosition: Float,
    headerHeightPx: Float,
    toolbarHeightPx: Float
) {
    val density = LocalDensity.current
    val collapseRange = headerHeightPx - toolbarHeightPx
    val collapseFraction = (scrollPosition / collapseRange).coerceIn(0f, 1f)
    
    val scaleXY = lerp(
        titleFontScaleStart.dp,
        titleFontScaleEnd.dp,
        collapseFraction
    )
    
    val titlePaddingStart = dimensionResource(R.dimen.padding_medium)
    val titlePaddingEnd = dimensionResource(R.dimen.toolbar_height)
    
    val titleX = lerp(
        titlePaddingStart,
        titlePaddingEnd,
        collapseFraction
    )
    
    val titleYStartPx = headerHeightPx * 0.75f
    val titleYEndPx = toolbarHeightPx / 2f
    val titleYPx = titleYStartPx + (titleYEndPx - titleYStartPx) * collapseFraction
    
    Text(
        text = stringResource(R.string.zoo_york),
        fontSize = dimensionResource(R.dimen.title_font_size).value.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier
            .graphicsLayer {
                translationX = with(density) { titleX.toPx() }
                translationY = titleYPx
                scaleX = scaleXY.value
                scaleY = scaleXY.value
                alpha = 1f - collapseFraction // Fade out as it collapses
            }
    )
}

@Composable
private fun Toolbar(
    scrollPosition: Float,
    headerHeightPx: Float,
    toolbarHeightPx: Float
) {
    val showToolbar = scrollPosition >= (headerHeightPx - toolbarHeightPx)

    AnimatedVisibility(
        visible = showToolbar,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        TopAppBar(
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xff026586), Color(0xff032C45))
                )
            ),
            navigationIcon = {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        .size(dimensionResource(R.dimen.icon_size_medium))
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            title = {   Text(
                text = stringResource(R.string.zoo_york),
                fontSize = dimensionResource(R.dimen.title_font_size).value.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                   )}
        )
    }
}

@Composable
private fun Body(
    listState: LazyListState,
    items: MutableList<ListItem>,
    onDismissScreen: (() -> Unit)?,
    headerHeight: Dp,
    modifier: Modifier = Modifier
) {
    val isAtTop = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && 
            listState.firstVisibleItemScrollOffset == 0
        }
    }
    
    LazyColumn(
        state = listState,
        modifier = modifier.then(
                if (onDismissScreen != null) {
                    Modifier.pointerInput(isAtTop.value) {
                        if (isAtTop.value) {
                            var totalDrag = 0f
                            detectVerticalDragGestures(
                                onDragEnd = {
                                    // Require a larger swipe than pull-to-refresh to dismiss screen
                                    if (totalDrag > screenDismissThreshold * 3) {
                                        onDismissScreen()
                                    }
                                    totalDrag = 0f
                                }
                            ) { change, dragAmount ->
                                // Only track significant downward drags
                                if (dragAmount > 0) {
                                    totalDrag += dragAmount
                                } else {
                                    totalDrag = 0f
                                }
                            }
                        }
                    }
                } else {
                    Modifier
                }
            ),
        contentPadding = PaddingValues(top = headerHeight),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            SwipeableListItem(
                item = item,
                onDismiss = {
                    items.remove(item)
                }
            )
        }
    }
}

@Composable
private fun SwipeableListItem(
    item: ListItem,
    onDismiss: () -> Unit
) {
    val density = LocalDensity.current
    val dismissState = remember(density) {
        SwipeToDismissBoxState(
            confirmValueChange = { value ->
                if (value == SwipeToDismissBoxValue.EndToStart || 
                    value == SwipeToDismissBoxValue.StartToEnd) {
                    onDismiss()
                    true
                } else {
                    false
                }
            },
            initialValue = SwipeToDismissBoxValue.Settled,
            positionalThreshold = { totalDistance -> totalDistance * 0.5f },
            density = density
        )
    }
    
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFB00020))
                    .padding(dimensionResource(R.dimen.padding_medium)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = Color.White,
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small))
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = item.content,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0XFF161616))
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
private fun Header(
    headerHeightPx: Float,
    scrollPosition: Float,
    headerHeight: Dp,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .height(headerHeight)) {
        // Header background with parallax effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Parallax: move at half the scroll speed
                    translationY = -scrollPosition * 0.5f
                    // Fade out as we scroll
                    alpha = 1f - (scrollPosition / headerHeightPx).coerceIn(0f, 1f)
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E3A5F),
                            Color(0xFF0A1F3A)
                        )
                    )
                )
        )
        
        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xAA000000)
                        ),
                        startY = 3 * headerHeightPx / 4
                    )
                )
        )
    }
}