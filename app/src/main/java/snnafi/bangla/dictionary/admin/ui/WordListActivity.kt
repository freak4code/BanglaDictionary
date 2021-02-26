package snnafi.bangla.dictionary.admin.ui


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import snnafi.bangla.dictionary.admin.R
import snnafi.bangla.dictionary.admin.adapter.ExampleLoadStateAdapter
import snnafi.bangla.dictionary.admin.adapter.WordListAdapter
import snnafi.bangla.dictionary.admin.databinding.WordListActivityBinding
import snnafi.bangla.dictionary.admin.ui.*
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.Helper
import snnafi.bangla.dictionary.admin.util.Helper.setTypeFace
import snnafi.bangla.dictionary.admin.util.WordListListener
import snnafi.bangla.dictionary.admin.viewmodel.WordViewModel
import kotlin.properties.Delegates

class WordListActivity : AppCompatActivity(), WordListListener {

    private lateinit var prefs: SharedPreferences
    private lateinit var binding: WordListActivityBinding
    private var page: Int? = null
    private val viewModel by viewModels<WordViewModel>()
    private val wordsAdapter = WordListAdapter(this)
    private var letterId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WordListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        letterId = intent.getIntExtra(Constant.WORD_ID, 0);
        binding.toolbar.title = Constant.allLetters.keys.toList()[letterId - 1]
        binding.cta.notFound.isVisible = false
        binding.cta.words.isVisible = true
        binding.progress.isVisible = true
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        setSupportActionBar(findViewById(R.id.toolbar))
        setTypeFace(listOf(binding.cta.notFound))
        checkNetwork()



        binding.cta.tryAgain.setOnClickListener {
            checkNetwork()
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            if (Helper.checkInternet(applicationContext)) {
                val intent = Intent(applicationContext, WordCommitActivity::class.java)
                intent.putExtra(Constant.WORD_ID, letterId)
                intent.putExtra(Constant.ID, true)
                resultLauncher.launch(intent)
            } else {
                Toast.makeText(applicationContext, "ইন্টারনেট কানেকশন নেই", Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    override fun onResume() {
        super.onResume()

    }


    private fun setupViews() {
        binding.cta.words.setHasFixedSize(true)
        binding.cta.words.layoutManager = LinearLayoutManager(this@WordListActivity)
        binding.cta.words.adapter = wordsAdapter
        wordsAdapter
            .withLoadStateHeaderAndFooter(
                header = ExampleLoadStateAdapter(wordsAdapter::retry),
                footer = ExampleLoadStateAdapter(wordsAdapter::retry)
            )
        wordsAdapter.registerAdapterDataObserver(adapterObserver)


    }

    private fun fetchPosts(letterId: Int) {
        lifecycleScope.launch {
            viewModel.fetchWords(letterId).collectLatest { pagingData ->
                wordsAdapter.submitData(pagingData)
            }
        }
    }

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {

            val count = wordsAdapter.itemCount
            if (itemCount == 0 && count == 0) {
                binding.cta.notFound.isVisible = true
                binding.cta.words.isVisible = false
                binding.progress.isVisible = false
            } else {
                binding.cta.notFound.isVisible = false
                binding.cta.words.isVisible = true
                binding.progress.isVisible = false
            }
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


    fun checkNetwork() {
        if (Helper.checkInternet(applicationContext)) {
            binding.cta.words.isVisible = true
            binding.cta.notFound.isVisible = false
            binding.cta.noConnection.isVisible = false
            setupViews()
            fetchPosts(letterId)
        } else {
            binding.cta.words.isVisible = false
            binding.cta.notFound.isVisible = false
            binding.cta.noConnection.isVisible = true
        }
    }


    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("TAGWL", "ID -> ${result.data?.getIntExtra(Constant.ID, 0)} ")
            if (result.resultCode == Activity.RESULT_OK && result.data?.getIntExtra(Constant.ID, 0) == 1) {
                binding.progress.isVisible = true
                fetchPosts(letterId)
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


}