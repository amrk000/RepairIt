package com.amrk000.repairit.presentation.screen.home

import android.app.Application
import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.ImageCapture
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amrk000.repairit.data.model.ResultData
import com.amrk000.repairit.domain.usecase.CaptureImageUseCase
import com.amrk000.repairit.domain.usecase.ProcessImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState (
    val isLoading: Boolean = false,
    val capturedImage: Bitmap? = null,
    val resultData: ResultData? = null,
    val error: String? = null
)

sealed class MainIntent {
    object captureImageAndAnalyze : MainIntent()
    data class analyzeImage(val image : Bitmap) : MainIntent()
    object resetState : MainIntent()
    data class setLoading(val loading : Boolean) : MainIntent()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val application: Application,
    private val captureImageUseCase: CaptureImageUseCase,
    private val processImageUseCase: ProcessImageUseCase
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val IMAGE_RESOLUTION = Size(640, 360)
    val imageCapture = ImageCapture.Builder().setTargetResolution(IMAGE_RESOLUTION).build()

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.captureImageAndAnalyze -> {
                captureImageAndAnalyze()
            }

            is MainIntent.analyzeImage -> {
                analyzeImage(intent.image)
            }

            is MainIntent.setLoading -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = intent.loading
                )
            }

            is MainIntent.resetState -> {
                _uiState.value = MainUiState()
            }
        }
    }

    private fun analyzeImage(image : Bitmap) {
        _uiState.value = _uiState.value.copy(
            capturedImage = image,
            isLoading = true
        )

        viewModelScope.launch {
            try {
                val resultData = processImageUseCase.invoke(image).first()

                if(resultData == null){
                    _uiState.value = _uiState.value.copy(
                        resultData = null,
                        error = "No Result"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        resultData = resultData
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()

                _uiState.value = _uiState.value.copy(
                    resultData = null,
                    error = e.message
                )
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false
            )
        }
    }

    private fun captureImageAndAnalyze(){
        viewModelScope.launch {
            captureImageUseCase.invoke(application, imageCapture).collect{
                it?.let {
                    analyzeImage(it)
                }
            }
        }
    }

}