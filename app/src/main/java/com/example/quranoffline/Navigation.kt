package com.example.quranoffline

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object AllReciter

@Serializable
data class Reciter(val reciterId: String)

@Serializable
object Radio

@Serializable
object Chapters

@Serializable
class ChapterScript(val chapterId: String)

@Serializable
object Books

@Serializable
data class BookChapters(val bookId: String)