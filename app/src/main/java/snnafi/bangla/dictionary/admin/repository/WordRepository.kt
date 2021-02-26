package snnafi.bangla.dictionary.admin.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import snnafi.bangla.dictionary.admin.datastore.WordDataSource
import snnafi.bangla.dictionary.admin.datastore.WordSearchedDataSource
import snnafi.bangla.dictionary.admin.model.Words

class WordRepository {

    fun fetchWords(letterId: Int): Flow<PagingData<Words>> {

        return Pager(
            PagingConfig(pageSize = 100, enablePlaceholders = false)
        ) {
            WordDataSource(letterId)
        }.flow
    }

    fun fetchSearchedWords(searchTerm: String): Flow<PagingData<Words>> {

        return Pager(
            PagingConfig(pageSize = 100, enablePlaceholders = false)
        ) {
            WordSearchedDataSource(searchTerm)
        }.flow
    }


}