package snnafi.bangla.dictionary.admin.model

import com.google.gson.annotations.SerializedName

data class WordResponse(
    var page: Int,
    var per_page: Int,
    var total_words: Int,
    var total_pages: Int,
    @SerializedName("vokti")
    var words: List<Words>?
)


data class Words(var id: Int, @SerializedName("vokti") var name: String)
