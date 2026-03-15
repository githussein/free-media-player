package com.example.quranoffline.ui.HadithScript

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quranoffline.data.Book
import com.example.quranoffline.data.BookChaptersResponse
import com.example.quranoffline.data.BookResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {
    private val _resultState = MutableStateFlow<BookResultState>(BookResultState.Loading)
    val resultState = _resultState.asStateFlow()

    private val _bookList = MutableStateFlow<List<Book>>(emptyList())
    val bookList = _bookList.asStateFlow()

    private val _book = MutableStateFlow<BookChaptersResponse?>(null)
    val book = _book.asStateFlow()

    init {
        // Removed fetchBooks() to allow LaunchedEffect in UI to handle it after locale resolution
    }

    fun fetchBooks() {
        viewModelScope.launch {
            _resultState.emit(BookResultState.Loading)

            try {
                val response = repository.getBooks()
                _resultState.emit(BookResultState.Success(response))
                _bookList.emit(response.books)
                println(response)
            } catch (e: Exception) {
                _resultState.emit(BookResultState.Failure(e))
                Log.e("BookViewModel", "Error fetching books: ${e.message}")
            }
        }
    }

    fun fetchBookById(id: String) {
        viewModelScope.launch {
            _resultState.emit(BookResultState.Loading)

            try {
                val response = repository.getBookById(id)
                _book.emit(response)
                _resultState.emit(BookResultState.BookSuccess(response))
                println(response)
            } catch (e: Exception) {
                _resultState.emit(BookResultState.Failure(e))
                Log.e("BookViewModel", "Error fetching book by id: ${e.message}")
            }
        }
    }

    fun fetchHadiths(bookSlug: String, chapterId: String) {
        viewModelScope.launch {
            _resultState.emit(BookResultState.Loading)
            try {
                val response = repository.getHadiths(bookSlug, chapterId)
                _resultState.emit(BookResultState.HadithSuccess(response))
            } catch (e: Exception) {
                _resultState.emit(BookResultState.Failure(e))
                Log.e("BookViewModel", "Error fetching hadiths: ${e.message}")
            }
        }
    }
}


sealed interface BookResultState {
    data object Loading : BookResultState
    data class Success(val response: com.example.quranoffline.data.BookResponse) : BookResultState
    data class BookSuccess(val response: com.example.quranoffline.data.BookChaptersResponse) : BookResultState
    data class HadithSuccess(val response: com.example.quranoffline.data.HadithListResponse) : BookResultState
    data class Failure(val e: Exception) : BookResultState
}