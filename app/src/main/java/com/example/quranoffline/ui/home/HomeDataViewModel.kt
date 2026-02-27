package com.example.quranoffline.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranoffline.data.Radio
import com.example.quranoffline.data.Reciter
import com.example.quranoffline.ui.Radio.IRadioRepository
import com.example.quranoffline.ui.reciters.IRecitersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeDataViewModel @Inject constructor(
    private val radioRepository: IRadioRepository,
    private val recitersRepository: IRecitersRepository
) : ViewModel() {

    private val _suggestedRadios = MutableStateFlow<List<Radio>>(emptyList())
    val suggestedRadios: StateFlow<List<Radio>> = _suggestedRadios.asStateFlow()

    private val _suggestedReciters = MutableStateFlow<List<Reciter>>(emptyList())
    val suggestedReciters: StateFlow<List<Reciter>> = _suggestedReciters.asStateFlow()

    init {
        fetchSuggestedRadios()
        fetchSuggestedReciters()
    }

    private fun fetchSuggestedRadios() {
        viewModelScope.launch {
            try {
                val radios = radioRepository.getRadioStations().radios
                _suggestedRadios.value = radios.shuffled().take(3)
            } catch (e: Exception) {
                _suggestedRadios.value = emptyList()
                Log.e("HomeDataViewModel", "Error fetching radios: ${e.message}")
            }
        }
    }

    private fun fetchSuggestedReciters() {
        viewModelScope.launch {
            try {
                val reciters = recitersRepository.getAllReciters().reciters
                _suggestedReciters.value = reciters.shuffled().take(3)
            } catch (e: Exception) {
                _suggestedReciters.value = emptyList()
                Log.e("HomeDataViewModel", "Error fetching reciters: ${e.message}")
            }
        }
    }
}