package snnafi.bangla.dictionary.admin.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import snnafi.bangla.dictionary.admin.R
import snnafi.bangla.dictionary.admin.model.Words
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.DiffUtilCallBack
import snnafi.bangla.dictionary.admin.util.Helper
import snnafi.bangla.dictionary.admin.util.WordListListener
import java.util.regex.Pattern

class WordListAdapter(val wordListListener: WordListListener) :
    PagingDataAdapter<Words, WordListAdapter.WordViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.word_list_item, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        getItem(position)?.let { word ->
            holder.bindWord(word)

        }

    }


    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordName: TextView = itemView.findViewById(R.id.wordName)
        private var word: Words? = null

        init {
            val context = itemView.context
            itemView.setOnClickListener {
                wordListListener.onSingleClick(word?.id, context)
            }

            itemView.setOnLongClickListener {
                wordListListener.onDoubleClick(word?.id, context)
                return@setOnLongClickListener true
            }
        }


        fun bindWord(word: Words) {
            this.word = word
            wordName.setText(
                Helper.duplicateWord(
                    word.name,
                    Pattern.compile("\\[.*?\\]")
                ),
                TextView.BufferType.SPANNABLE
            )

            Typeface.createFromAsset(itemView.context.assets, Constant.APP_FONT)
                .apply {
                    wordName.typeface = this
                }
            animateView(itemView)
        }


        private fun animateView(viewToAnimation: View) {
            if (viewToAnimation.animation == null) {
                val animation = AnimationUtils.loadAnimation(viewToAnimation.context, R.anim.scaled)
                viewToAnimation.animation = animation
            }
        }
    }

}








