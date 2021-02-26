package snnafi.bangla.dictionary.admin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snnafi.bangla.dictionary.admin.adapter.WordAdapter
import snnafi.bangla.dictionary.admin.databinding.ActivityMainBinding
import snnafi.bangla.dictionary.admin.model.User
import snnafi.bangla.dictionary.admin.ui.*
import snnafi.bangla.dictionary.admin.util.Constant


class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupViews()
        getUserDetails()
        Log.d("USER_ROLE", prefs.getInt(Constant.ROLE, -1).toString())

    }

    override fun onResume() {
        super.onResume()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {

            R.id.search -> {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                return true
            }

            R.id.about -> {
                startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                return true
            }
            R.id.userProfile -> {
                startActivity(Intent(this@MainActivity, UserProfile::class.java))
                return true
            }
            R.id.logOut -> {
                prefs.edit().putBoolean(Constant.IS_LOGIN, false).apply()
                prefs.edit().putBoolean(Constant.FETCH_USER, false).apply()
                prefs.edit().putInt(Constant.ID, -1).apply()
                prefs.edit().putString(Constant.NAME, "").apply()
                prefs.edit().putString(Constant.EMAIL, "").apply()
                prefs.edit().putInt(Constant.ROLE, -1).apply()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews() {
        binding.cta.words.setHasFixedSize(true)
        binding.cta.words.layoutManager =
            GridLayoutManager(applicationContext, 4, GridLayoutManager.VERTICAL, false)
        binding.cta.words.adapter = WordAdapter()


    }


    private fun getUserDetails() {
        if (!prefs.getBoolean(Constant.FETCH_USER, false)) {
            BanglaDictionary.banglaDictionaryApi()
                .getUserDetail(Constant.API_KEY, prefs.getString(Constant.EMAIL, "")!!).enqueue(
                    object : Callback<List<User>> {

                        override fun onResponse(
                            call: Call<List<User>>,
                            response: Response<List<User>>
                        ) {

                            response.body()?.first()?.let {
                                prefs.edit().putInt(Constant.ID, it.id).apply()
                                prefs.edit().putString(Constant.NAME, it.name).apply()
                                prefs.edit().putString(Constant.EMAIL, it.email).apply()
                                prefs.edit().putInt(Constant.ROLE, it.role).apply()
                                prefs.edit().putBoolean(Constant.FETCH_USER, true).apply()

                                Toast.makeText(
                                    this@MainActivity,
                                    "Welcome \n${prefs.getString(Constant.NAME, "")!!}",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }


                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            Toast.makeText(
                                this@MainActivity,
                                "Something went wrong\n${t.localizedMessage}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                )
        }
    }
}