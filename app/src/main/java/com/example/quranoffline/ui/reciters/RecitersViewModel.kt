package com.example.quranoffline.ui.reciters

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranoffline.data.Reciter
import com.example.quranoffline.data.ReciterResponse
import com.example.quranoffline.data.Surah
import com.example.quranoffline.data.SurahUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReciterViewModel @Inject constructor(
    private val repository: RecitersRepository
) : ViewModel() {

    private val _resultState = MutableStateFlow<RecitationsResultState>(RecitationsResultState.Idle)
    val resultState = _resultState.asStateFlow()

    private val _selectedReciter = mutableStateOf<Reciter?>(null)
    val selectedReciter: State<Reciter?> = _selectedReciter

    private val _surahList = MutableStateFlow<List<SurahUi>>(emptyList())
    val surahList = _surahList.asStateFlow()


    init {
        fetchReciters()
    }

    private fun fetchReciters() {
        viewModelScope.launch {
            _resultState.emit(RecitationsResultState.Loading)

            try {
                val response = repository.getAllReciters()
                _resultState.emit(RecitationsResultState.Success(response))
            } catch (e: Exception) {
                _resultState.emit(RecitationsResultState.Failure(e))
                Log.e("ReciterViewModel", "Error fetching reciters: ${e.message}")
            }
        }
    }

    fun fetchReciterById(id: String) {
        viewModelScope.launch {
            _resultState.emit(RecitationsResultState.Loading)
            try {
                val response = repository.getReciterById(reciterId = id)

                _selectedReciter.value = response.reciters.firstOrNull()
                _resultState.emit(RecitationsResultState.Success(response))

                val availableSurahId = response.reciters.firstOrNull()?.moshaf?.firstOrNull()?.surah_list?.split(",")?.map { it.toInt() }?.toList()

                val surahList = fetchSurahList()

                val surahNameList = availableSurahId?.map { surahId ->
                    val surah = surahList.firstOrNull { it.id == surahId }
                    SurahUi(surah = surah, server = response.reciters.firstOrNull()?.moshaf?.firstOrNull()?.server?.formatServerUrl(surahId))
                }
                _surahList.emit(surahNameList.orEmpty())

            } catch (e: Exception) {
                _resultState.emit(RecitationsResultState.Failure(e))
            }
        }
    }

    suspend fun fetchSurahList(): List<Surah> {
        return try {
            val response = repository.getSurahList()
            response.suwar
        } catch (e: Exception) {
            Log.e("ReciterViewModel", "Error fetching surahs: ${e.message}")
            emptyList()
        }
    }

}

sealed interface RecitationsResultState {
    data object Idle : RecitationsResultState
    data object Loading : RecitationsResultState
    data class Success(val response: ReciterResponse) : RecitationsResultState
    data class Failure(val e: Exception) : RecitationsResultState
}