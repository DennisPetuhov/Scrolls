package com.example.scrolls.repository

import com.example.scrolls.data.CatImage
import com.example.scrolls.data.NetworkModule

class CatRepository {
    private val apiService = NetworkModule.catApiService

    suspend fun getCatImages(limit: Int = 10): Result<List<CatImage>> {
        return try {
            val images = apiService.searchImages(limit = limit)
            Result.success(images)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}