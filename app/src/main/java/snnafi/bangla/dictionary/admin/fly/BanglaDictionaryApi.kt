package snnafi.bangla.dictionary.admin.fly

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import snnafi.bangla.dictionary.admin.model.About
import snnafi.bangla.dictionary.admin.model.User
import snnafi.bangla.dictionary.admin.model.Word
import snnafi.bangla.dictionary.admin.model.WordResponse

interface BanglaDictionaryApi {

    @GET("words.php")
    suspend fun getAllWords(
        @Query("api_key") api_key: String,
        @Query("page") page: Int? = null,
        @Query("vokti_id") letterId: Int
    ): WordResponse


    @GET("word_search.php")
    suspend fun getAllFoundWords(
        @Query("api_key") api_key: String,
        @Query("page") page: Int? = null,
        @Query("search_term") searchTerm: String
    ): WordResponse

    @GET("word_detail.php")
    fun getWordDetail(@Query("api_key") api_key: String, @Query("id") id: Int): Call<List<Word>>


    @GET("user_detail.php")
    fun getUserDetail(
        @Query("api_key") api_key: String,
        @Query("email") email: String? = null,
        @Query("id") id: Int? = null
    ): Call<List<User>>

    @FormUrlEncoded
    @POST("update_word_info.php")
    fun updateWord(
        @FieldMap field: Map<String, String>
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("new_word.php")
    fun addWord(
        @FieldMap field: Map<String, String>
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("delete_word.php")
    fun deleteWord(
        @FieldMap field: Map<String, String>
    ): Call<ResponseBody>

    @GET("user_login.php")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("user_register.php")
    fun register(@FieldMap field: Map<String, String>): Call<ResponseBody>

    @GET("about.php")
    fun about(
        @Query("api_key") api_key: String,
    ): Call<List<About>>
}


