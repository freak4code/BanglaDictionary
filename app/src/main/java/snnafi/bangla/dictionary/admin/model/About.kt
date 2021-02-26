package snnafi.bangla.dictionary.admin.model

import com.google.gson.annotations.SerializedName

class About(
    @SerializedName("total_words")
    val totalWord: Int,
    @SerializedName("total_user")
    val totalUser: Int
) {
}