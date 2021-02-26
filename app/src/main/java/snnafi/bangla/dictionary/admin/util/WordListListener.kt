package snnafi.bangla.dictionary.admin.util

import android.content.Context
import snnafi.bangla.dictionary.admin.model.Word

interface WordListListener {
    fun onSingleClick(wordId: Int?, context: Context)
    fun onDoubleClick(wordId: Int?, context: Context)
}