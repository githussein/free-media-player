package com.example.quranoffline.ui.HadithScript

import com.example.quranoffline.data.BookChaptersResponse
import com.example.quranoffline.data.BookResponse
import com.example.quranoffline.data.BookService
import javax.inject.Inject
import javax.inject.Singleton

interface IBooksRepository {
    suspend fun getBooks(): BookResponse
    suspend fun getBookById(bookSlug: String): BookChaptersResponse
    suspend fun getHadiths(bookSlug: String, chapterId: String): com.example.quranoffline.data.HadithListResponse
}

@Singleton
class BookRepository @Inject constructor(
    private val apiService: BookService
) : IBooksRepository {

    private var cachedBooks: BookResponse? = null
    private val cachedChapters = mutableMapOf<String, BookChaptersResponse>()
    private val cachedHadiths = mutableMapOf<String, com.example.quranoffline.data.HadithListResponse>()

    override suspend fun getBooks(): BookResponse {
        cachedBooks?.let { return it }
        val response = apiService.api.getBooks()
        cachedBooks = response
        return response
    }

    override suspend fun getBookById(bookSlug: String): BookChaptersResponse {
        cachedChapters[bookSlug]?.let { return it }
        val response = apiService.api.getBookById(bookSlug)
        cachedChapters[bookSlug] = response
        return response
    }

    override suspend fun getHadiths(bookSlug: String, chapterId: String): com.example.quranoffline.data.HadithListResponse {
        val cacheKey = "$bookSlug-$chapterId"
        cachedHadiths[cacheKey]?.let { return it }
        val response = apiService.api.getHadiths(book = bookSlug, chapter = chapterId)
        cachedHadiths[cacheKey] = response
        return response
    }
}