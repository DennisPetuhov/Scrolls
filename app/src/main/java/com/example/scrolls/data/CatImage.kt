package com.example.scrolls.data

data class CatImage(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
    val breeds: List<Breed>? = null,
    val categories: List<Category>? = null
)

data class Breed(
    val id: String,
    val name: String,
    val temperament: String? = null,
    val origin: String? = null,
    val description: String? = null
)

data class Category(
    val id: Int,
    val name: String
)
