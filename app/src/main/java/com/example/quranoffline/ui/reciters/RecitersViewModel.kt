package com.example.quranoffline.ui.reciters

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranoffline.data.Moshaf
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
    private val repository: IRecitersRepository
) : ViewModel() {

    private val _resultState = MutableStateFlow<RecitationsResultState>(RecitationsResultState.Loading)
    val resultState = _resultState.asStateFlow()

    private val _selectedReciter = mutableStateOf<Reciter?>(null)
    val selectedReciter: State<Reciter?> = _selectedReciter

    private val _surahList = MutableStateFlow<List<SurahUi>>(emptyList())
    val surahList = _surahList.asStateFlow()

    private val _selectedMoshaf = MutableStateFlow<Moshaf?>(null)
    val selectedMoshaf = _selectedMoshaf.asStateFlow()


    fun fetchReciters() {
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
                val reciter = response.reciters.firstOrNull()

                _selectedReciter.value = reciter
                _selectedMoshaf.value = reciter?.moshaf?.firstOrNull()

                _resultState.emit(RecitationsResultState.Success(response))
                buildSurahList()
            } catch (e: Exception) {
                _resultState.emit(RecitationsResultState.Failure(e))
            }
        }
    }

    fun selectMoshaf(moshaf: Moshaf) {
        _selectedMoshaf.value = moshaf
        buildSurahList()
    }

    private fun buildSurahList() {
        viewModelScope.launch {
            val moshaf = _selectedMoshaf.value ?: return@launch

            val availableSurahIds = moshaf.surah_list
                .split(",")
                .mapNotNull { it.toIntOrNull() }

            val allSurahs = fetchSurahList()

            val surahUiList = availableSurahIds.mapNotNull { surahId ->
                allSurahs.firstOrNull { it.id == surahId }?.let { surah ->
                    SurahUi(
                        surah = surah,
                        server = moshaf.server.formatServerUrl(surahId)
                    )
                }
            }

            _surahList.emit(surahUiList)
        }
    }

    suspend fun fetchSurahList(): List<Surah> = try {
        repository.getSurahList().suwar
    } catch (e: Exception) {
        Log.e("ReciterViewModel", "Error fetching surahs: ${e.message}")
        emptyList()
    }
}

sealed interface RecitationsResultState {
    data object Loading : RecitationsResultState
    data class Success(val response: ReciterResponse) : RecitationsResultState
    data class Failure(val e: Exception) : RecitationsResultState
}