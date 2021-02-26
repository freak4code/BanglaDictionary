package snnafi.bangla.dictionary.admin.ui

import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snnafi.bangla.dictionary.admin.BanglaDictionary
import snnafi.bangla.dictionary.admin.databinding.UserProfileBinding
import snnafi.bangla.dictionary.admin.model.User
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.Helper.setTypeFace


class UserProfile : AppCompatActivity() {

    private lateinit var binding: UserProfileBinding
    private lateinit var prefs: SharedPreferences
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTypeFace(listOf(binding.nameUser, binding.userEmail, binding.userId))
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        id = intent.getIntExtra(Constant.ID, -1)
        if (id == -1) {
            prefs = PreferenceManager.getDefaultSharedPreferences(this)
            binding.userId.text = prefs.getInt(Constant.ID, -1).toString()
            binding.nameUser.text = prefs.getString(Constant.NAME, "Not Found")
            binding.userEmail.text = prefs.getString(Constant.EMAIL, "Not Found")
        } else {
            title = "Collector Info"
            binding.progress.isVisible = true
            getUserDetails()
        }

    }

    private fun getUserDetails() {

        BanglaDictionary.banglaDictionaryApi().getUserDetail(Constant.API_KEY, id = id).enqueue(
            object : Callback<List<User>> {

                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {
                    binding.progress.isVisible = false
                    response.body()?.first()?.let {
                        binding.nameUser.text = it.name

                    }

                }


                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Toast.makeText(
                        this@UserProfile,
                        "Something went wrong\n${t.localizedMessage}",
                        Toast.LENGTH_LONG
                    )
                        .show()

                    binding.userId.text = (-1).toString()
                    binding.nameUser.text = "Not Found"
                    binding.userEmail.text = "Not Found"
                }

            }
        )
    }




}