package snnafi.bangla.dictionary.admin.adapter

import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import snnafi.bangla.dictionary.admin.R
import snnafi.bangla.dictionary.admin.ui.WordListActivity
import snnafi.bangla.dictionary.admin.util.Constant

class WordAdapter : RecyclerView.Adapter<WordAdapter.WordHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordHolder {
        return WordHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        )
    }


    override fun onBindViewHolder(holder: WordHolder, position: Int) {

        holder.bindView(Constant.allLetters.values.toList()[position])

    }

    override fun getItemCount(): Int {
        return Constant.allLetters.size
    }


    class WordHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.letterName);
        private var letterId: Int? = null

        init {
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, WordListActivity::class.java)
                intent.putExtra(Constant.WORD_ID, letterId)
                context.startActivity(intent)
            }

        }

        fun bindView(letterId: Int) {
            this.letterId = letterId
            textView.setText(Constant.allLetters.keys.toList()[letterId - 1])
            Typeface.createFromAsset(itemView.context.assets, Constant.APP_FONT)
                .apply {
                    textView.typeface = this
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