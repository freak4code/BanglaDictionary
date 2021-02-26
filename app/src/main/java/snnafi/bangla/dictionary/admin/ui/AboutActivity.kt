package snnafi.bangla.dictionary.admin.ui

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snnafi.bangla.dictionary.admin.BanglaDictionary
import snnafi.bangla.dictionary.admin.R
import snnafi.bangla.dictionary.admin.databinding.AboutBinding
import snnafi.bangla.dictionary.admin.model.About
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.Helper.replaceBanglaNumber
import snnafi.bangla.dictionary.admin.util.Helper.setTypeFace


class AboutActivity : AppCompatActivity() {

    private lateinit var binding: AboutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTypeFace(listOf(binding.totalUser, binding.totalWord))
        binding.progress.isVisible = true
        binding.totalUser.isVisible = false
        binding.totalWord.isVisible = false

        BanglaDictionary.banglaDictionaryApi().about(Constant.API_KEY)
            .enqueue(object : Callback<List<About>> {

                override fun onResponse(
                    call: Call<List<About>>,
                    response: Response<List<About>>
                ) {
                    response.body()?.first()?.let {
                        binding.totalWord.setText(
                            resources.getString(
                                R.string.totalWord,
                                replaceBanglaNumber(it.totalWord.toString())
                            )
                        )
                        binding.totalUser.setText(
                            resources.getString(
                                R.string.totalUser,
                                replaceBanglaNumber(it.totalUser.toString())
                            )
                        )

                        binding.progress.isVisible = false
                        binding.totalUser.isVisible = true
                        binding.totalWord.isVisible = true

                    }
                }


                override fun onFailure(call: Call<List<About>>, t: Throwable) {
                    Toast.makeText(
                        this@AboutActivity,
                        "Something went wrong\n${t.localizedMessage}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

            })
    }



}