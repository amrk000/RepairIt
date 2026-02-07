package com.amrk000.repairit.domain.repository

import android.graphics.Bitmap

interface IGeminiRepository {
    suspend fun diagnoseItem(image: Bitmap): String?
}