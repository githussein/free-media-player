package com.example.quranoffline.extensions

fun String.convertToArabicIndicNumbers() =
    this.map {
        if (it in '0'..'9') it + ('٠' - '0')
        else it
    }.joinToString("")

val String.fixHafsFont: String
    get() = this.replace("\u06DF", "\u0652")