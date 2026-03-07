package com.example.quranoffline.util

import java.util.Locale

object LocaleHelper {

    fun getApiLanguage(): String {
        return when (Locale.getDefault().language) {
            "ar" -> "ar"
            else -> "eng"
        }
    }
}