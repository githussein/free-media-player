package com.example.quranoffline.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranoffline.data.Radio
import com.example.quranoffline.ui.Radio.RadioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RadioRepository
) : ViewModel() {

    private val _suggestedRadios = MutableStateFlow<List<Radio>>(emptyList())
    val suggestedRadios = _suggestedRadios.asStateFlow()

    init {
        fetchRandomRadios()
    }

    private fun fetchRandomRadios() {
        viewModelScope.launch {
            try {
                val radios = repository.getRadioStations().radios
                _suggestedRadios.value = radios.shuffled().take(3)
            } catch (e: Exception) {
                _suggestedRadios.value = emptyList()
            }
        }
    }
}