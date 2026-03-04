package com.example.quranoffline.ui.ChapterScript

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranoffline.data.ChapterScriptResponse
import com.example.quranoffline.data.ChaptersResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChaptersViewModel @Inject constructor(
    private val repository: ChaptersRepository
) : ViewModel() {
    private val _resultState = MutableStateFlow<ChaptersResultState>(ChaptersResultState.Loading)
    val resultState = _resultState.asStateFlow()

    private val _chapterScript = MutableStateFlow<ChapterScriptResponse?>(null)
    val chapterScript = _chapterScript.asStateFlow()

    init {
        fetchChapters()
    }

    private fun fetchChapters() {
        viewModelScope.launch {
            _resultState.emit(ChaptersResultState.Loading)

            try {
                val response = repository.getChapters()
                println(response)
                _resultState.emit(ChaptersResultState.Success(response))
            } catch (e: Exception) {
                _resultState.emit(ChaptersResultState.Failure(e))
                Log.e("ChaptersViewModel", "Error fetching chapters: ${e.message}")
            }
        }
    }

    fun getChapterScript(chapterId: String) {
        viewModelScope.launch {
            _resultState.emit(ChaptersResultState.Loading)

            try {
                val response = repository.getChapterScript(chapterId)
                _chapterScript.emit(response)
                _resultState.emit(ChaptersResultState.ScriptSuccess(response))
                println(response)
            } catch (e: Exception) {
                _resultState.emit(ChaptersResultState.Failure(e))
                Log.e("ChaptersViewModel", "Error fetching script: ${e.message}")
            }
        }
    }
}

sealed interface ChaptersResultState {
    data object Loading : ChaptersResultState
    data class Success(val response: ChaptersResponse) : ChaptersResultState
    data class ScriptSuccess(val response: ChapterScriptResponse) : ChaptersResultState
    data class Failure(val e: Exception) : ChaptersResultState
}