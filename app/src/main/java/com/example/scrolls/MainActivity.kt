package com.example.scrolls

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.MainThread
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.scrolls.ui.CatFormScreen
import com.example.scrolls.ui.theme.ScrollsTheme
import okhttp3.internal.http2.Http2Reader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScrollsTheme {
                CatFormScreen()
//                NestedScrollExample (categories = sampleCategories)
//                CollapsingToolBarParallax  ()
//                ComposeInViewGroupExample()
            }
        }
//        Thread.sleep(1000)
//        startActivity(Intent(this, CoordinatorLayoutActivity::class.java))
    }
}