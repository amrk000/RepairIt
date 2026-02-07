package com.amrk000.repairit.data.remote

import android.app.Application
import android.graphics.Bitmap
import com.amrk000.repairit.BuildConfig
import com.amrk000.repairit.R
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class GeminiService @Inject constructor(
    private val context: Application
) {
    private val generativeModel = GenerativeModel(
        modelName = context.getString(
            R.string.model_gemini_3_pro
        ),
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun processPrompt(prompt: String, image: Bitmap?=null): String? {
        val inputContent = content {
            text(prompt)
            image?.let { image(image) }
        }

        return generativeModel.generateContent(inputContent).text
    }
}