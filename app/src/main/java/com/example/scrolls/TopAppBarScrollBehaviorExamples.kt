@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.scrolls

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ExitEnterTopAppBar() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_app)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            items(100) { index ->
                Text(
                    text = stringResource(R.string.item, index),
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun PinnedTopAppBar() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.pinned_bar)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            items(100) { index ->
                Text(
                    text = stringResource(R.string.item, index),
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun EnterAlwaysTopAppBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.enter_always)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            items(100) { index ->
                Text(
                    text = stringResource(R.string.item, index),
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomScrollBehaviorTopAppBar() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val scrollFraction = scrollBehavior.state.overlappedFraction
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.scroll_percentage, (scrollFraction * 100).toInt()),
                        color = Color.White.copy(alpha = 1f - scrollFraction)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = stringResource(R.string.menu),
                            tint = Color.White.copy(alpha = 1f - scrollFraction)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Blue.copy(alpha = 1f - scrollFraction * 0.5f)
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            items(100) { index ->
                Text(
                    text = stringResource(R.string.item, index),
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun LargeTopAppBarWithScroll() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.large_title)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            items(100) { index ->
                Text(
                    text = stringResource(R.string.item, index),
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
fun PinnedLargeTopAppBar() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.pinned_large)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            items(100) { index ->
                Text(
                    text = stringResource(R.string.item, index),
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(name = "Enter Always Large")
@Composable
fun EnterAlwaysLargeTopAppBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.enter_always_large)) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            items(100) { index ->
                Text(
                    text = stringResource(R.string.item, index),
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}