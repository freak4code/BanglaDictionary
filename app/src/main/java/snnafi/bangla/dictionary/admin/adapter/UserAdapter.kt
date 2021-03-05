package snnafi.bangla.dictionary.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import snnafi.bangla.dictionary.admin.R
import snnafi.bangla.dictionary.admin.model.WordAddingInfo
import snnafi.bangla.dictionary.admin.util.Helper

class UserAdapter(val wordAddingInfoList: List<WordAddingInfo>) :
    RecyclerView.Adapter<UserAdapter.UserHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        )
    }


    override fun onBindViewHolder(holder: UserHolder, position: Int) {

        holder.bindView(wordAddingInfoList[position])

    }

    override fun getItemCount(): Int {
        return wordAddingInfoList.size
    }


    inner class UserHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.userName);
        private val addedWord = itemView.findViewById<TextView>(R.id.addedWord);


        init {

        }

        fun bindView(wordAddingInfo: WordAddingInfo) {
            Helper.setTypeFace(listOf(name, addedWord))
            name.text = wordAddingInfo.name
            addedWord.text =
                itemView.context.resources.getString(R.string.addedWord, wordAddingInfo.wordAdded)


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
