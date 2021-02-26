package snnafi.bangla.dictionary.admin.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import snnafi.bangla.dictionary.admin.MainActivity
import snnafi.bangla.dictionary.admin.databinding.SplashActivityBinding
import snnafi.bangla.dictionary.admin.util.Constant
import java.util.*

class SplashActivity : AppCompatActivity() {
    private var isLogin = false
    private lateinit var prefs: SharedPreferences

    private lateinit var binding: SplashActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashActivityBinding.inflate(layoutInflater)
        makeFullScreen()
        prefs = PreferenceManager.getDefaultSharedPreferences(this@SplashActivity)
        setContentView(binding.root)
        isLogin = prefs.getBoolean(Constant.IS_LOGIN, false)
        Log.d("isLogin", isLogin.toString())
        Typeface.createFromAsset(applicationContext.assets, Constant.APP_FONT).apply {
            binding.textView.setTypeface(this)

        }

        Timer().schedule(
            object : TimerTask() {
                override fun run() {


                    if (isLogin) {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        Log.d("MainActivity", isLogin.toString())
                    } else {
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        Log.d("LoginActivity", isLogin.toString())
                    }

                    finish()

                }
            },
            4000
        )

    }

    private fun makeFullScreen() {
        // Remove Title
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Make Fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Hide the toolbar
        supportActionBar?.hide()
    }
}