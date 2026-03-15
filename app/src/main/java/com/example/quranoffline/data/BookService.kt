package com.example.quranoffline.data

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val API_KEY = "\$2y\$10\$2QgUUnZmmsTuBgBb7Su9G5RpM2BljtFjKinMWPO8QprEN878C"


@Module
@InstallIn(SingletonComponent::class)
object BookService {
    val api: BookService by lazy {
        Retrofit.Builder()
            .baseUrl("https://hadithapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookService::class.java)
    }

    interface BookService {
        @GET("books")
        suspend fun getBooks(@Query("apiKey") apiKey: String = API_KEY): BookResponse

        @GET("{bookSlug}/chapters")
        suspend fun getBookById(
            @Path("bookSlug") bookSlug: String,
            @Query("apiKey") apiKey: String = API_KEY
        ): BookChaptersResponse

        @GET("hadiths")
        suspend fun getHadiths(
            @Query("apiKey") apiKey: String = API_KEY,
            @Query("book") book: String,
            @Query("chapter") chapter: String,
            @Query("paginate") paginate: Int = 100
        ): HadithListResponse
    }
}


data class BookResponse(
    val status: Int,
    val message: String,
    val books: List<Book>
)

data class Book(
    val id: Int,
    val bookName: String,
    val writerName: String,
    val aboutWriter: String?,
    val writerDeath: String,
    val bookSlug: String,
    val hadiths_count: String,
    val chapters_count: String
)


data class BookChaptersResponse(
    val status: Int,
    val message: String,
    val chapters: List<Chapter>
)

data class Chapter(
    val id: Int,
    val chapterNumber: String,
    val chapterEnglish: String,
    val chapterUrdu: String,
    val chapterArabic: String,
    val bookSlug: String
)

data class HadithListResponse(
    val status: Int,
    val message: String,
    val hadiths: HadithData
)

data class HadithData(
    val data: List<Hadith>
)

data class Hadith(
    val id: Int,
    val hadithNumber: String,
    val englishNarrator: String?,
    val hadithEnglish: String?,
    val hadithArabic: String?,
    val hadithUrdu: String?,
    val bookSlug: String,
    val chapterId: String
)