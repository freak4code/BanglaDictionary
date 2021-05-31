package snnafi.bangla.dictionary.admin.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snnafi.bangla.dictionary.admin.BanglaDictionary
import snnafi.bangla.dictionary.admin.R
import snnafi.bangla.dictionary.admin.adapter.UniversalAdapter
import snnafi.bangla.dictionary.admin.databinding.WordCommitBinding
import snnafi.bangla.dictionary.admin.model.Word
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.Constant.allLetters
import snnafi.bangla.dictionary.admin.util.Helper
import snnafi.bangla.dictionary.admin.util.Helper.setTypeFace
import snnafi.bangla.dictionary.admin.util.Helper.validate
import java.util.*
import kotlin.collections.HashMap


class WordCommitActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var binding: WordCommitBinding
    private lateinit var word: Word
    private var wordId = 0
    private lateinit var prefs: SharedPreferences
    private val visibility = mapOf("Invisible" to 0, "Visible" to 1)
    private var isNew = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WordCommitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        // If new word, wordId carries the [letter id] and if old word, wordId carries the [word id]
        wordId = intent.getIntExtra(Constant.WORD_ID, 0)
        isNew = intent.getBooleanExtra(Constant.ID, false)
        binding.commit.isEnabled = false
        binding.delete.isEnabled = false

        if (isNew) {
            title = "Add New"
            word = Word(-1, wordId, "", "", "", "", "", "", "", "", 0, 1)
            setLetterSpinner()
            setVisibilitySpinner()
            Helper.addNil(
                listOf(
                    binding.wordUccharon,
                    binding.wordButpotti,
                    binding.wordOrtho,
                    binding.striBachok,
                    binding.wordTuloniyo,
                    binding.wordProyogbakko,
                    binding.wordPorivasha,
                )
            )
            binding.commit.isEnabled = true
        } else {
            binding.progress.isVisible = true
            title = "Update"
            getWordById()

        }
        setTypeFace(
            listOf(
                binding.one,
                binding.two,
                binding.three,
                binding.four,
                binding.five,
                binding.six,
                binding.seven,
                binding.eight,
                binding.nine,
                binding.ten,
                binding.name,
                binding.wordUccharon,
                binding.wordButpotti,
                binding.striBachok,
                binding.wordOrtho,
                binding.wordTuloniyo,
                binding.wordProyogbakko,
                binding.wordPorivasha
            )
        )




        binding.commit.setOnClickListener {

            if (Helper.checkInternet(applicationContext))
                if (validate(
                        listOf(
                            binding.name,
                            binding.wordUccharon,
                            binding.wordButpotti,
                            binding.wordOrtho,
                            binding.striBachok,
                            binding.wordTuloniyo,
                            binding.wordProyogbakko,
                            binding.wordPorivasha,

                            )
                    )
                ) {

                    binding.progress.isVisible = true
                    word.name = binding.name.text.toString()
                    word.uccharon = binding.wordUccharon.text.toString()
                    word.bupotti = binding.wordButpotti.text.toString()
                    word.striBachok = binding.striBachok.text.toString()
                    word.ortho = binding.wordOrtho.text.toString()
                    word.tuloniyo = binding.wordTuloniyo.text.toString()
                    word.proyogBakko = binding.wordProyogbakko.text.toString()
                    word.porivasha = binding.wordPorivasha.text.toString()
                    word.addedBy = prefs.getInt(Constant.ID, 0);


                    val wordReq = HashMap<String, String>()
                    wordReq.put("api_key", Constant.API_KEY)
                    wordReq.put(
                        "vokti_id",
                        (binding.letterSpinner.selectedItemPosition + 1).toString()
                    )
                    wordReq.put("vokti", word.name)
                    wordReq.put("uccharon", word.uccharon)
                    wordReq.put("bupotti", word.bupotti)
                    wordReq.put("stri_bachok", word.striBachok)
                    wordReq.put("ortho", word.ortho)
                    wordReq.put("tuloniyo", word.tuloniyo)
                    wordReq.put("proyogbakko", word.proyogBakko)
                    wordReq.put("porivasha", word.porivasha)
                    wordReq.put("added_by", word.addedBy.toString())
                    wordReq.put("listed", binding.visibilitySpinner.selectedItemPosition.toString())


                    if (word.id == -1) {
                        Log.d("QueryMap", wordReq.toString())
                        BanglaDictionary.banglaDictionaryApi().addWord(wordReq)
                            .enqueue(object : Callback<ResponseBody> {

                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {

                                    binding.progress.isVisible = false
                                    val responseString = response.body()?.string()
                                    Log.d("Response", responseString.toString() ?: "")

                                    Toast.makeText(
                                        this@WordCommitActivity,
                                        responseString.toString().trim(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    intent.putExtra(Constant.ID, 1)
                                    setResult(RESULT_OK, intent)
                                    onBackPressed()

                                }


                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    binding.progress.isVisible = false

                                    Toast.makeText(
                                        this@WordCommitActivity,
                                        "Something went wrong\n${t.localizedMessage}",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                }

                            })
                    } else {
                        wordReq.put("id", word.id.toString())
                        Log.d("FieldMap", wordReq.toString())
                        BanglaDictionary.banglaDictionaryApi().updateWord(wordReq)
                            .enqueue(object : Callback<ResponseBody> {

                                override fun onResponse(
                                    call: Call<ResponseBody>,
                                    response: Response<ResponseBody>
                                ) {

                                    binding.progress.isVisible = false
                                    val responseString = response.body()?.string()
                                    Log.d("Response", responseString.toString() ?: "")


                                    Toast.makeText(
                                        this@WordCommitActivity,
                                        responseString.toString().trim(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent = Intent()
                                    Log.d("TAG", "onResponse: ${word.name.trim()} --- ${prefs.getString(Constant.IS_NAME_SEDITED, "")}")
                                    if (word.name.trim()
                                            .equals(prefs.getString(Constant.IS_NAME_SEDITED, ""))
                                    ) {
                                        intent.putExtra(Constant.ID, 0)
                                    } else {
                                        intent.putExtra(Constant.ID, 1)
                                    }
                                    setResult(RESULT_OK, intent)
                                    onBackPressed()

                                }


                                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                    binding.progress.isVisible = false

                                    Toast.makeText(
                                        this@WordCommitActivity,
                                        "Something went wrong\n${t.localizedMessage}",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                }

                            })

                    }
                } else {
                    Toast.makeText(
                        this@WordCommitActivity,
                        getString(R.string.fill_all),
                        Toast.LENGTH_LONG
                    ).show()
                }
            else {
                Toast.makeText(applicationContext, "ইন্টারনেট কানেকশন নেই", Toast.LENGTH_LONG)
                    .show()
            }

        }



        binding.delete.setOnClickListener {
            if (Helper.checkInternet(applicationContext)) {
                binding.progress.isVisible = true
                val wordReq = HashMap<String, String>()
                wordReq.put("api_key", Constant.API_KEY)
                wordReq.put("id", word.id.toString())
                wordReq.put("vokti", word.name)
                BanglaDictionary.banglaDictionaryApi().deleteWord(wordReq)
                    .enqueue(object : Callback<ResponseBody> {

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {

                            binding.progress.isVisible = false
                            val responseString = response.body()?.string()
                            Log.d("Response", responseString.toString() ?: "")

                            Toast.makeText(
                                this@WordCommitActivity,
                                responseString.toString().trim(),
                                Toast.LENGTH_LONG
                            ).show()
                            intent.putExtra(Constant.ID, 1)
                            setResult(RESULT_OK, intent)
                            onBackPressed()

                        }


                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            binding.progress.isVisible = false

                            Toast.makeText(
                                this@WordCommitActivity,
                                "Something went wrong\n${t.localizedMessage}",
                                Toast.LENGTH_LONG
                            )
                                .show()

                        }

                    })
            } else {
                Toast.makeText(applicationContext, "ইন্টারনেট কানেকশন নেই", Toast.LENGTH_LONG)
                    .show()
            }

        }


    }

    fun getWordById() {
        BanglaDictionary.banglaDictionaryApi().getWordDetail(Constant.API_KEY, this.wordId)
            .enqueue(object : Callback<List<Word>> {

                override fun onResponse(call: Call<List<Word>>, response: Response<List<Word>>) {
                    binding.progress.isVisible = false
                    response.body()?.first()?.let {
                        word = it
                        checkUser();
                        prefs.edit().putString(Constant.IS_NAME_SEDITED, it.name.trim()).apply()
                        binding.commit.text = resources.getString(R.string.updateWord)
                        binding.name.setText(word.name)
                        binding.wordUccharon.setText(word.uccharon)
                        binding.wordButpotti.setText(word.bupotti)
                        binding.striBachok.setText(word.striBachok)
                        binding.wordOrtho.setText(word.ortho)
                        binding.wordTuloniyo.setText(word.tuloniyo)
                        binding.wordProyogbakko.setText(word.proyogBakko)
                        binding.wordPorivasha.setText(word.porivasha)
                        setLetterSpinner()
                        setVisibilitySpinner()



                    }
                }


                override fun onFailure(call: Call<List<Word>>, t: Throwable) {
                    Toast.makeText(
                        this@WordCommitActivity,
                        "Something went wrong\n${t.localizedMessage}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

            })
    }

    private fun setLetterSpinner() {
        binding.letterSpinner.setOnItemSelectedListener(this)
        val wordVisibility = UniversalAdapter(
            allLetters.keys.toList(),
            (allLetters.values).toList()
        )
        binding.letterSpinner.adapter = wordVisibility
        binding.letterSpinner.setSelection(word.letterId)
        if (word.letterId > 0 && word.letterId <= allLetters.size) {
            binding.letterSpinner.setSelection(word.letterId - 1)
        } else {
            binding.letterSpinner.setSelection(0)
        }

    }

    private fun setVisibilitySpinner() {
        binding.visibilitySpinner.setOnItemSelectedListener(this)
        val visibilitySpinner = UniversalAdapter(
            visibility.keys.toList(),
            (visibility.values).toList()
        )
        binding.visibilitySpinner.adapter = visibilitySpinner
        if (word.listed < 2) {
            binding.visibilitySpinner.setSelection(word.listed)
        } else {
            binding.visibilitySpinner.setSelection(0)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            R.id.letterSpinner -> {
                word.letterId = p2 + 1
                binding.letterSpinner.setSelection(p2)
                Log.d("LS", p2.toString())

            }
            R.id.visibilitySpinner -> {
                word.listed = p2
                Log.d("VS", p2.toString())
                binding.visibilitySpinner.setSelection(p2)
            }

        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    fun checkUser() {
        if (prefs.getInt(Constant.ROLE, -1) == 1) {
            binding.commit.isEnabled = true
            binding.delete.isEnabled = true

        } else if (prefs.getInt(Constant.ROLE, -1) == 2) {
            if (prefs.getInt(Constant.ID, -1) == word.addedBy) {
                binding.commit.isEnabled = true
            } else {
                Toast.makeText(
                    applicationContext,
                    "কেবল মাত্র আপনার অ্যাড করা শব্দ এডিট করতে পারবেন !",
                    Toast.LENGTH_LONG
                ).show()
                onBackPressed()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "আপনার রুল যাচাইকৃত নয় !",
                Toast.LENGTH_LONG
            ).show()
            onBackPressed()
        }
    }


}