package snnafi.bangla.dictionary.admin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import snnafi.bangla.dictionary.admin.model.Words
import snnafi.bangla.dictionary.admin.repository.WordRepository

class WordViewModel(application: Application) : AndroidViewModel(application) {

    // 1
    private val wordRepo = WordRepository()

    // 2
    fun fetchWords(letterId: Int): Flow<PagingData<Words>> {
        return wordRepo.fetchWords(letterId).cachedIn(viewModelScope)
    }

    fun fetchSearchedWords(searchTerm: String): Flow<PagingData<Words>> {
        return wordRepo.fetchSearchedWords(searchTerm).cachedIn(viewModelScope)
    }


}