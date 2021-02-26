package snnafi.bangla.dictionary.admin.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import snnafi.bangla.dictionary.admin.BanglaDictionary
import snnafi.bangla.dictionary.admin.databinding.WordDetailBinding
import snnafi.bangla.dictionary.admin.model.Word
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.Helper
import snnafi.bangla.dictionary.admin.util.Helper.setTypeFace
import snnafi.bangla.dictionary.admin.util.PatternEditableBuilder
import snnafi.bangla.dictionary.admin.util.PatternEditableBuilderTwo
import java.util.*
import java.util.regex.Pattern


class WordDetailActivity : AppCompatActivity() {

    private lateinit var binding: WordDetailBinding
    private var wordId: Int = 0
    private lateinit var prefs: SharedPreferences
    private lateinit var word: Word
    //  private lateinit var images


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WordDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        binding.progress.isVisible = true
        binding.creditId.isEnabled = false
        wordId = intent.getIntExtra(Constant.WORD_ID, 0)
        if (wordId > 0) {
            BanglaDictionary.banglaDictionaryApi().getWordDetail(Constant.API_KEY, wordId)
                .enqueue(object : Callback<List<Word>> {

                    override fun onResponse(
                        call: Call<List<Word>>,
                        response: Response<List<Word>>
                    ) {
                        binding.progress.isVisible = false
                        response.body()?.first()?.let {
                            word = it
                            binding.creditId.isEnabled = true
                            binding.wordName.setUpTextTitle(it.name);
                            binding.wordUccharon.setUpText(it.uccharon);
                            binding.wordButpotti.setUpText(Helper.replaceShortForm(it.bupotti), true);
                            binding.striBachok.setUpText(Helper.replaceShortForm(it.striBachok));
                            binding.wordOrtho.setUpText(Helper.replaceShortForm(it.ortho));
                            binding.wordTuloniyo.setUpText(it.tuloniyo);
                            binding.wordProyogbakko.setUpText(Helper.replaceShortForm(it.proyogBakko));
                            binding.wordPorivasha.setUpText(it.porivasha);


                        }
                    }


                    override fun onFailure(call: Call<List<Word>>, t: Throwable) {
                        Toast.makeText(
                            this@WordDetailActivity,
                            "Something went wrong\n${t.localizedMessage}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

                })
        }


        binding.creditId.setOnClickListener {
            if (Helper.checkInternet(applicationContext)) {
                val intent = Intent(this@WordDetailActivity, UserProfile::class.java)
                intent.putExtra(Constant.ID, word.addedBy)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "ইন্টারনেট কানেকশন নেই", Toast.LENGTH_LONG)
                    .show()
            }
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
                binding.creditId,
            )
        )

    }


    fun TextView.setUpTextTitle(text: String) {
        setText(
            Helper.duplicateWord(
                text,
                Pattern.compile("\\[.*?\\]")
            ),
            TextView.BufferType.SPANNABLE
        )
        setTypeFace(listOf(this))

    }


    fun TextView.setUpText(text: String, replace: Boolean = false) {
        if (replace == false) {
            this.text = text
        } else {
            this.text =
                text.replace("rt", Constant.BUTPOTTI_SYMBOL)

        }

        setTypeFace(listOf(this))
        PatternEditableBuilder(applicationContext).addPattern(Pattern.compile("\\[.*?\\]"))
            .addPattern(Pattern.compile("\\{.*?\\}"))
            .addPattern(Pattern.compile("#.*?#"))
            .addPattern(
                Pattern.compile("@.*?@"),
                object : PatternEditableBuilder.SpannableClickedListener {
                    override fun onSpanClicked(text: String?) {
                        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                    }

                }).into(
                this
            )


    }


}