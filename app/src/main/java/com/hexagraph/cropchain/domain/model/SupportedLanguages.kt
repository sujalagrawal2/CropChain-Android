package com.hexagraph.cropchain.domain.model

enum class SupportedLanguages(val languageNameInNormalScript: String,
                              val languageNameInNativeScript: String,
                              val langID: Int,
    val languageCode: String
    ) {
    ENGLISH("English","English", 1, "en"),
    HINDI("Hindi", "हिन्दी", 2, "hi"),
    TELUGU("Telugu","తెలుగు", 3, "te"),
    TAMIL("Tamil","தமிழ்", 4, "ta"),
    KANNADA("Kannada","ಕನ್ನಡ", 5, "kn"),
    MARATHI("Marathi","मराठी", 6, "mr"),
}