package com.example.scrolls

import android.view.View
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.example.scrolls.ui.theme.ScrollsTheme

//https://medium.com/androiddevelopers/understanding-nested-scrolling-in-jetpack-compose-eb57c1ea0af0
class ViewGroupNestedScrollConnection(
    private val view: View,
    private val isAtTopState: () -> Boolean
) : NestedScrollConnection {
    
    private var activeScrollAxis: Int = 0

    private var totalConsumedY: Int = 0
    
    init {
        ViewCompat.setNestedScrollingEnabled(view, true)
    }

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val dx = available.x.toInt()
        val dy = available.y.toInt()
        
        if (dx == 0 && dy == 0) {
            return Offset.Zero
        }
        
        val scrollAxis = when {
            dy != 0 -> ViewCompat.SCROLL_AXIS_VERTICAL
            dx != 0 -> ViewCompat.SCROLL_AXIS_HORIZONTAL
            else -> 0
        }
        
        if (activeScrollAxis == 0 && scrollAxis != 0) {
            val started = ViewCompat.startNestedScroll(view, scrollAxis, 0)
            if (started) {
                activeScrollAxis = scrollAxis
                totalConsumedY = 0 // Reset tracking when starting new session
            } else {
                return Offset.Zero
            }
        }
        

        if (activeScrollAxis != 0) {

            val consumedArray = intArrayOf(0, 0)
            ViewCompat.dispatchNestedPreScroll(view, dx, dy, consumedArray, null)
            
            totalConsumedY += consumedArray[1]
            
            return Offset(consumedArray[0].toFloat(), consumedArray[1].toFloat())
        }
        
        return Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        if (activeScrollAxis == 0) {
            return Offset.Zero
        }
        
        val dxConsumed = consumed.x.toInt()
        val dyConsumed = consumed.y.toInt()
        val dxUnconsumed = available.x.toInt()
        var dyUnconsumed = available.y.toInt()
        
        totalConsumedY += dyConsumed
        

        val isAtTop = isAtTopState()
        if (isAtTop && dyConsumed < 0) {

            dyUnconsumed = dyUnconsumed - dyConsumed
        }
        
        ViewCompat.dispatchNestedScroll(
            view,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            null
        )

        return Offset.Zero
    }
    
    override suspend fun onPreFling(available: Velocity): Velocity {
        if (activeScrollAxis == 0) {
            return Velocity.Zero
        }
        
        val consumed = ViewCompat.dispatchNestedPreFling(view, available.x, available.y)
        return if (consumed == true) {
            Velocity(available.x, available.y)
        } else {
            Velocity.Zero
        }
    }


    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        if (activeScrollAxis == 0) {
            return Velocity.Zero
        }
        
        val hasConsumedVelocity = consumed.x != 0f || consumed.y != 0f
        ViewCompat.dispatchNestedFling(
            view,
            available.x,
            available.y,
            hasConsumedVelocity
        )
        
        ViewCompat.stopNestedScroll(view, activeScrollAxis)
        activeScrollAxis = 0
        totalConsumedY = 0 // Reset tracking
        
        return Velocity.Zero
    }
}

@Composable
fun ComposeInViewGroupExample(
    modifier: Modifier = Modifier,
    items: List<String> = sampleScrollItems,
    rowState: LazyListState = rememberLazyListState(),
    columnState: LazyListState = rememberLazyListState()
) {
    val view = LocalView.current
    
    val composeView = remember(view) {
        var current: View? = view
        while (current != null) {
            if (current.parent is CoordinatorLayout) {
                return@remember current
            }
            current = (current.parent as? View)
        }
        view
    }
    
    val isAtTop by remember {
        derivedStateOf {
            columnState.firstVisibleItemIndex == 0 && columnState.firstVisibleItemScrollOffset == 0
        }
    }
    
    val isAtTopState = rememberUpdatedState(isAtTop)
    
    val nestedScrollConnection = remember(composeView) {
        ViewGroupNestedScrollConnection(composeView) { isAtTopState.value }
    }

    LazyColumn(
        state = columnState,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.scroll_down_to_see_appbar_collapse),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        
        item {
            Text(
                text = stringResource(R.string.horizontal_scroll_section),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyRow(
                state = rowState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(
                    items = items,
                    key = { it }
                ) { item ->
                    ScrollItemCard(item = item)
                }
            }
        }
        
        items(20) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            R.string.vertical_item_scroll_to_collapse_appbar,
                            index + 1
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun ScrollItemCard(
    item: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(150.dp)
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

val sampleScrollItems = listOf(
    "Item 1",
    "Item 2",
    "Item 3",
    "Item 4",
    "Item 5",
    "Item 6",
    "Item 7",
    "Item 8",
    "Item 9",
    "Item 10",
    "Item 11",
    "Item 12",
    "Item 13",
    "Item 14",
    "Item 15"
)

@Preview(showBackground = true)
@Composable
fun ComposeInViewGroupExamplePreview() {
    ScrollsTheme {
        ComposeInViewGroupExample()
    }
}