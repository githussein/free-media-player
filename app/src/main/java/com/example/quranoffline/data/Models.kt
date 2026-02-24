package com.example.quranoffline.data

data class Reciter(
    val id: Int,
    val name: String,
    val letter: String,
    val moshaf: List<Moshaf>
)

data class Moshaf(
    val id: Int,
    val name: String,
    val server: String,
    val surah_total: Int,
    val moshaf_type: Int,
    val surah_list: String
) {
    companion object {
        fun generateRandomMoshafList(): List<Moshaf> {
            return List((1..3).random()) {
                Moshaf(
                    id = 1,
                    name = "Murattal",
                    server = "",
                    surah_total = 10,
                    moshaf_type = 1,
                    surah_list = "1, 2, 3"
                )
            }
        }
    }
}


data class Surah(
    val id: Int,
    val name: String,
    val start_page: Int,
    val end_page: Int,
    val makkia: Int,
    val type: Int
)

data class SurahUi(
    val surah: Surah?,
    val server: String?
)

data class Radio(
    val id: Int,
    val name: String,
    val url: String
)

data class ReciterResponse(val reciters: List<Reciter>)
data class SurahResponse(val suwar: List<Surah>)
data class RadioResponse(val radios: List<Radio>)