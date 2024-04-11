package com.who.simplelogin.models

data class MUser(
    val id: String,
    val displayName: String,
    val quote: String,
    val profession: String
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "user_id" to id,
            "display_Name" to displayName,
            "quote" to quote,
            "profession_name" to profession
        )
    }
}
