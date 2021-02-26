package snnafi.bangla.dictionary.admin.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import snnafi.bangla.dictionary.admin.R
import snnafi.bangla.dictionary.admin.adapter.WordListAdapter
import snnafi.bangla.dictionary.admin.adapter.WordListLoadingAdapter
import snnafi.bangla.dictionary.admin.databinding.SearchActivityBinding
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.Helper
import snnafi.bangla.dictionary.admin.util.WordListListener
import snnafi.bangla.dictionary.admin.viewmodel.WordViewModel


class SearchActivity : AppCompatActivity(), WordListListener {

    private lateinit var prefs: SharedPreferences
    private lateinit var binding: SearchActivityBinding
    private val viewModel by viewModels<WordViewModel>()
    private lateinit var wordsAdapter: WordListAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupViews()
        setTypeFace(listOf(binding.searchTerm))

        binding.searchButton.setOnClickListener {
            if (validate(listOf(binding.searchTerm))) {
                binding.progress.isVisible = true
                fetchSearchedPosts("%${binding.searchTerm.text.toString().trim()}%")
            } else {
                Toast.makeText(
                    this@SearchActivity,
                    getString(R.string.fill_all),
                    Toast.LENGTH_LONG
                ).show();
            }

        }

    }

    override fun onResume() {
        super.onResume()

    }


    private fun setupViews() {
        binding.words.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this@SearchActivity)
        binding.words.layoutManager = linearLayoutManager
        wordsAdapter = WordListAdapter(this)
        binding.words.adapter = wordsAdapter
        binding.words.adapter = wordsAdapter.withLoadStateHeaderAndFooter(
            header = WordListLoadingAdapter { wordsAdapter.retry() },
            footer = WordListLoadingAdapter { wordsAdapter.retry() }
        )
        wordsAdapter.registerAdapterDataObserver(adapterObserver)


    }

    private fun fetchSearchedPosts(searchTerm: String) {

        lifecycleScope.launch {
            viewModel.fetchSearchedWords(searchTerm).collectLatest { pagingData ->
                print("DATA: ${pagingData}");
                wordsAdapter.submitData(pagingData)

            }
        }
    }

    private fun validate(fields: List<EditText>): Boolean {
        for (i in fields.indices) {
            val currentField = fields[i]
            if (currentField.text.toString().length <= 0) {
                return false
            }
        }
        return true
    }

    fun setTypeFace(textViews: List<TextView>) {
        Typeface.createFromAsset(applicationContext.assets, Constant.APP_FONT).apply {
            textViews.forEach {
                it.setTypeface(this)
            }
        }
    }

    override fun onSingleClick(wordId: Int?, context: Context) {
        if (Helper.checkInternet(context)) {
            Log.d("FGHJKGFD", "SINGLE")
            val intent = Intent(context, WordDetailActivity::class.java)
            intent.putExtra(Constant.WORD_ID, wordId)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "ইন্টারনেট কানেকশন নেই", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onDoubleClick(wordId: Int?, context: Context) {
        if (Helper.checkInternet(context)) {
            Log.d("FGHJKGFD", "DOUBLE")
            val intent = Intent(context, WordCommitActivity::class.java)
            intent.putExtra(Constant.WORD_ID, wordId)
            resultLauncher.launch(intent)

        } else {
            Toast.makeText(context, "ইন্টারনেট কানেকশন নেই", Toast.LENGTH_LONG)
                .show()
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.getIntExtra(
                    Constant.ID,
                    0
                ) == 1
            ) {
                if (validate(listOf(binding.searchTerm))) {
                    binding.progress.isVisible = true
                    fetchSearchedPosts("%${binding.searchTerm.text.toString().trim()}%")
                } else {
                    Toast.makeText(
                        this@SearchActivity,
                        getString(R.string.fill_all),
                        Toast.LENGTH_LONG
                    ).show();
                }
            }

        }

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            Log.d("SearchActivity", "onItemRangeInserted: ")
            val count = wordsAdapter.itemCount
            if (itemCount == 0 && count == 0) {
                binding.notFound.isVisible = true
                binding.words.isVisible = false
                binding.progress.isVisible = false
            } else {
                binding.notFound.isVisible = false
                binding.words.isVisible = true
                binding.progress.isVisible = false
            }
            linearLayoutManager.scrollToPositionWithOffset(0, 10)

        }

        override fun onChanged() {
            super.onChanged()
            binding.progress.isVisible = false
            Log.d("SearchActivity", "onChanged: ")
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            binding.progress.isVisible = false
            Log.d("SearchActivity", "onItemRangeChanged: ")
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            binding.progress.isVisible = false
            Log.d("SearchActivity", "onItemRangeRemoved: ")
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            binding.progress.isVisible = false
            Log.d("SearchActivity", "onItemRangeMoved: ")
        }


    }

}