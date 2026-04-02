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

    private val _isRadiosLoading = MutableStateFlow(false)
    val isRadiosLoading: StateFlow<Boolean> = _isRadiosLoading.asStateFlow()

    private val _isRecitersLoading = MutableStateFlow(false)
    val isRecitersLoading: StateFlow<Boolean> = _isRecitersLoading.asStateFlow()

    private var currentRadiosLanguage: String = ""
    private var currentRecitersLanguage: String = ""

    fun fetchSuggestedRadios() {
        val lang = com.example.quranoffline.util.LocaleHelper.getApiLanguage()
        if (_suggestedRadios.value.isNotEmpty() && currentRadiosLanguage == lang) return
        currentRadiosLanguage = lang
        
        _isRadiosLoading.value = true
        viewModelScope.launch {
            try {
                val radios = radioRepository.getRadioStations().radios
                _suggestedRadios.value = radios.shuffled().take(3)
            } catch (e: Exception) {
                _suggestedRadios.value = emptyList()
                Log.e("HomeDataViewModel", "Error fetching radios: ${e.message}")
            } finally {
                _isRadiosLoading.value = false
            }
        }
    }

    fun fetchSuggestedReciters() {
        val lang = com.example.quranoffline.util.LocaleHelper.getApiLanguage()
        if (_suggestedReciters.value.isNotEmpty() && currentRecitersLanguage == lang) return
        currentRecitersLanguage = lang
        
        _isRecitersLoading.value = true
        viewModelScope.launch {
            try {
                val reciters = recitersRepository.getAllReciters().reciters
                _suggestedReciters.value = reciters.shuffled().take(3)
            } catch (e: Exception) {
                _suggestedReciters.value = emptyList()
                Log.e("HomeDataViewModel", "Error fetching reciters: ${e.message}")
            } finally {
                _isRecitersLoading.value = false
            }
        }
    }
}