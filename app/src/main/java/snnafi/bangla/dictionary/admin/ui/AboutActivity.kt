package snnafi.bangla.dictionary.admin.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snnafi.bangla.dictionary.admin.BanglaDictionary
import snnafi.bangla.dictionary.admin.R
import snnafi.bangla.dictionary.admin.adapter.UserAdapter
import snnafi.bangla.dictionary.admin.databinding.AboutBinding
import snnafi.bangla.dictionary.admin.model.About
import snnafi.bangla.dictionary.admin.model.WordAddingInfo
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.Helper.replaceBanglaNumber
import snnafi.bangla.dictionary.admin.util.Helper.setTypeFace


class AboutActivity : AppCompatActivity() {

    private lateinit var binding: AboutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTypeFace(
            listOf(
                binding.totalUser,
                binding.totalWord,
                binding.addToday,
                binding.addAllTime
            )
        )
        binding.progress.isVisible = true
        binding.totalUser.isVisible = false
        binding.totalWord.isVisible = false
        binding.usersToday.isVisible = false
        binding.usersAllTime.isVisible = false
        binding.addToday.isVisible = false
        binding.addAllTime.isVisible = false

        BanglaDictionary.banglaDictionaryApi().about(Constant.API_KEY)
            .enqueue(object : Callback<List<About>> {

                override fun onResponse(
                    call: Call<List<About>>,
                    response: Response<List<About>>
                ) {
                    response.body()?.first()?.let { it ->
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

                        Log.d("TAG", "onResponse: ${response.body()?.first()?.todayAddByUser}")
                        Log.d("TAG", "onResponse: ${response.body()?.first()?.allTimeAddByUser}")

                        todayAdded(response)
                        allTimeAdded(response)
                        binding.progress.isVisible = false
                        binding.totalUser.isVisible = true
                        binding.totalWord.isVisible = true
                        binding.usersToday.isVisible = true
                        binding.usersAllTime.isVisible = true
                        binding.addToday.isVisible = true
                        binding.addAllTime.isVisible = true

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

    fun todayAdded(response: Response<List<About>>) {
        binding.usersToday.setHasFixedSize(true)
        binding.usersToday.layoutManager = LinearLayoutManager(applicationContext)
        val nameList =
            response.body()?.first()?.todayAddByUser?.groupingBy { it.name }
                ?.eachCount()?.filter { it.value > 0 }
        val wordAddingInfos = mutableListOf<WordAddingInfo>()
        if (nameList != null) {
            for (i in 0 until nameList.count()) {
                wordAddingInfos.add(
                    WordAddingInfo(
                        nameList.keys.toList()[i],
                        replaceBanglaNumber(nameList.values.toList()[i].toString())

                    )
                )

            }
        }
        binding.usersToday.adapter = UserAdapter(wordAddingInfos)
    }

    fun allTimeAdded(response: Response<List<About>>) {
        binding.usersAllTime.setHasFixedSize(true)
        binding.usersAllTime.layoutManager = LinearLayoutManager(applicationContext)
        val allTimeAddByUser =  response.body()?.first()?.allTimeAddByUser?.filterNotNull()
        val allTimeList =
            allTimeAddByUser?.groupingBy { it.name }
                ?.eachCount()?.filter { it.value > 0 }
        val wordAddingInfos = mutableListOf<WordAddingInfo>()
        if (allTimeList != null) {
            for (i in 0 until allTimeList.count()) {
                wordAddingInfos.add(
                    WordAddingInfo(
                        allTimeList.keys.toList()[i],
                        replaceBanglaNumber(allTimeList.values.toList()[i].toString())

                    )
                )

            }
        }
        binding.usersAllTime.adapter = UserAdapter(wordAddingInfos)
    }


}