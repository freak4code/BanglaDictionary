package snnafi.bangla.dictionary.admin.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snnafi.bangla.dictionary.admin.BanglaDictionary
import snnafi.bangla.dictionary.admin.R
import snnafi.bangla.dictionary.admin.databinding.RegisterUiBinding
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.Helper
import snnafi.bangla.dictionary.admin.util.Helper.setTypeFace
import snnafi.bangla.dictionary.admin.util.Helper.validate


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterUiBinding
    private var isLogin: Boolean = false
    private lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterUiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        isLogin = prefs.getBoolean(Constant.IS_LOGIN, false)

        setTypeFace(listOf(binding.title, binding.email, binding.password, binding.name))


        binding.register.setOnClickListener {
            if (Helper.checkInternet(applicationContext)) {
                if (validate(listOf(binding.name, binding.email, binding.password))) {

                    val registerReq = HashMap<String, String>()
                    registerReq.put("api_key", Constant.API_KEY)
                    registerReq.put("name", binding.name.text.toString().trim())
                    registerReq.put("email", binding.email.text.toString().trim())
                    registerReq.put("password", binding.password.text.toString().trim())
                    BanglaDictionary.banglaDictionaryApi().register(registerReq).enqueue(
                        object : Callback<ResponseBody> {

                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {

                                val responseString = response.body()?.string().toString().trim()
                                Log.d("Response", responseString)
                                if (responseString == "SUCCESS") {
//                            prefs.edit().putBoolean(Constant.IS_LOGIN, true).apply()
//                            prefs.edit().putString(Constant.EMAIL, binding.email.text.toString().trim()).apply()
//                            prefs.edit().putString(Constant.NAME, binding.name.text.toString().trim())
//                                .apply()
//                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
//                            intent.flags =
//                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            startActivity(intent)
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Register Successfully! Please verify your account !",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                    onBackPressed()

                                } else {
                                    Toast.makeText(
                                        this@RegisterActivity, responseString,
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }


                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Something went wrong\n${t.localizedMessage}",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }

                        }
                    )
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        getString(R.string.fill_all),
                        Toast.LENGTH_LONG
                    ).show();
                }
            } else {
                Toast.makeText(applicationContext, "ইন্টারনেট কানেকশন নেই", Toast.LENGTH_LONG)
                    .show()
            }
        }

        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }



}