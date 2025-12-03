package com.example.scrolls

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.scrolls.ui.theme.ScrollsTheme

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