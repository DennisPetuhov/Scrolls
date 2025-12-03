package com.example.scrolls.data

import retrofit2.http.GET
import retrofit2.http.Query

interface CatApiService {
    @GET("images/search")
    suspend fun searchImages(
        @Query("limit") limit: Int = 10,
        @Query("size") size: String = "med",
        @Query("mime_types") mimeTypes: String = "jpg,png"
    ): List<CatImage>
}
