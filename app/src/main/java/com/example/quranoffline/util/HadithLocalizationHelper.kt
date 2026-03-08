package com.example.quranoffline.util

object HadithLocalizationHelper {

    private val bookNameMapAr = mapOf(
        "sahih-bukhari" to "صحيح البخاري",
        "sahih-muslim" to "صحيح مسلم",
        "al-tirmidhi" to "جامع الترمذي",
        "abu-dawood" to "سنن أبي داود",
        "ibn-e-majah" to "سنن ابن ماجه",
        "sunan-nasai" to "سنن النسائي",
        "mishkat" to "مشكاة المصابيح",
        "musnad-ahmad" to "مسند أحمد",
        "al-silsila-sahiha" to "السلسلة الصحيحة"
    )

    private val writerNameMapAr = mapOf(
        "Imam Bukhari" to "الإمام البخاري",
        "Imam Muslim" to "الإمام مسلم",
        "Abu `Isa Muhammad at-Tirmidhi" to "أبو عيسى محمد الترمذي",
        "Imam Abu Dawud Sulayman ibn al-Ash'ath as-Sijistani" to "الإمام أبو داود السجستاني",
        "Imam Muhammad bin Yazid Ibn Majah al-Qazvini" to "الإمام محمد بن ماجه القزويني",
        "Imam Ahmad an-Nasa`i" to "الإمام أحمد النسائي",
        "Imam Khatib at-Tabrizi" to "الإمام الخطيب التبريزي",
        "Imam Ahmad ibn Hanbal" to "الإمام أحمد بن حنبل",
        "Allama Muhammad Nasir Uddin Al-Bani" to "العلامة محمد ناصر الدين الألباني"
    )

    fun getLocalizedBookName(slug: String, defaultName: String): String {
        return if (LocaleHelper.getApiLanguage() == "ar") {
            bookNameMapAr[slug] ?: defaultName
        } else {
            defaultName
        }
    }

    fun getLocalizedWriterName(name: String): String {
        return if (LocaleHelper.getApiLanguage() == "ar") {
            writerNameMapAr[name] ?: name
        } else {
            name
        }
    }
}
