package com.amrk000.repairit.presentation.screen.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amrk000.repairit.data.model.HistoryItem
import com.amrk000.repairit.domain.usecase.GetScanHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val application: Application,
    private val getScanHistoryUseCase: GetScanHistoryUseCase
) : AndroidViewModel(application) {

    val history = MutableStateFlow<List<HistoryItem>>(listOf())

    fun loadHistory(){
        viewModelScope.launch {
            history.value = getScanHistoryUseCase.invoke()
        }
    }

}