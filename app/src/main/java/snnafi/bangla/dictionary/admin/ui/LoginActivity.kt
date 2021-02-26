package snnafi.bangla.dictionary.admin.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snnafi.bangla.dictionary.admin.BanglaDictionary
import snnafi.bangla.dictionary.admin.MainActivity
import snnafi.bangla.dictionary.admin.R
import snnafi.bangla.dictionary.admin.databinding.LoginUiBinding
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.Helper
import snnafi.bangla.dictionary.admin.util.Helper.setTypeFace
import snnafi.bangla.dictionary.admin.util.Helper.validate

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginUiBinding
    private var isLogin: Boolean = false
    private lateinit var prefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginUiBinding.inflate(layoutInflater)
        setContentView(binding.root)


        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        isLogin = prefs.getBoolean(Constant.IS_LOGIN, false)
        setTypeFace(listOf(binding.title, binding.email, binding.password))

        binding.register.setOnClickListener {
            Log.d("jhgf", "onCreate: ")
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener {
            if (Helper.checkInternet(applicationContext)) {
                if (validate(listOf(binding.email, binding.password))) {
                    Log.d("Email", binding.email.text.toString())
                    binding.progress.isVisible = true
                    BanglaDictionary.banglaDictionaryApi().login(
                        binding.email.text.toString(),
                        binding.password.text.toString()
                    ).enqueue(object : Callback<ResponseBody> {

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {

                            binding.progress.isVisible = false
                            val responseString = response.body()?.string()
                            Log.d("Response", responseString.toString() ?: "")
                            Log.d("LoginResponse", response.body()!!.string())
                            if (responseString!!.toString().trim() == "NOTFOUND") {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Not Found this account",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else if (responseString.toString().trim() == "FAILED") {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Password don't match",
                                    Toast.LENGTH_LONG
                                ).show()

                            } else {
                                val name = responseString.trim()
                                prefs.edit().putBoolean(Constant.IS_LOGIN, true).apply()
                                prefs.edit()
                                    .putString(Constant.EMAIL, binding.email.text.toString())
                                    .apply()
                                prefs.edit().putString(Constant.NAME, name).apply()

                                Toast.makeText(this@LoginActivity, "Success !", Toast.LENGTH_LONG)
                                    .show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        }


                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            binding.progress.isVisible = false

                            Toast.makeText(
                                this@LoginActivity,
                                "Something went wrong\n${t.localizedMessage}",
                                Toast.LENGTH_LONG
                            )
                                .show()

                        }

                    })
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.fill_all),
                        Toast.LENGTH_LONG
                    ).show();
                }

            } else {
                Toast.makeText(applicationContext, "ইন্টারনেট কানেকশন নেই",Toast.LENGTH_LONG).show()
            }

        }


    }




}