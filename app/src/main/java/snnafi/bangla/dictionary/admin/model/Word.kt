package snnafi.bangla.dictionary.admin.model

import com.google.gson.annotations.SerializedName

data class Word(
    var id: Int = 0,
    @SerializedName("vokti_id")
    var letterId: Int,
    @SerializedName("vokti")
    var name: String,
    @SerializedName("uccharon")
    var uccharon: String,
    @SerializedName("bupotti")
    var bupotti: String,
    @SerializedName("stri_bachok")
    var striBachok: String,
    @SerializedName("ortho")
    var ortho: String,
    @SerializedName("tuloniyo")
    var tuloniyo: String,
    @SerializedName("proyogbakko")
    var proyogBakko: String,
    @SerializedName("porivasha")
    var porivasha: String,
    @SerializedName("added_by")
    var addedBy: Int,
    var listed: Int = 1
)




