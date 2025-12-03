package com.example.scrolls

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.ViewCompat
import com.example.scrolls.ui.theme.ScrollsTheme

class CoordinatorLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator_compose)
        
        val composeView = findViewById<ComposeView>(R.id.composeView)
        
        ViewCompat.setNestedScrollingEnabled(composeView, true)
        
        composeView.setContent {
            ScrollsTheme {
                ComposeInViewGroupExample()
            }
        }
        
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
