package snnafi.bangla.dictionary.admin.model

import com.google.gson.annotations.SerializedName

class About(
    @SerializedName("total_words")
    val totalWord: Int,
    @SerializedName("total_user")
    val totalUser: Int,
    @SerializedName("today_add")
    val todayAddByUser: List<UserInfo>,
    @SerializedName("alltime_add")
    val allTimeAddByUser: List<UserInfo>
) {

    inner class UserInfo(val id: Int, val name: String)
}