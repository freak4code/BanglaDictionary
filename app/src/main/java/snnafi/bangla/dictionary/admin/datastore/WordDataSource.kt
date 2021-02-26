package snnafi.bangla.dictionary.admin.datastore

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import snnafi.bangla.dictionary.admin.BanglaDictionary
import snnafi.bangla.dictionary.admin.model.Words
import snnafi.bangla.dictionary.admin.util.Constant
import java.io.IOException

class WordDataSource(val letterId: Int) : PagingSource<Int, Words>() {
    /**
     * Provide a [Key] used for the initial [load] for the next [PagingSource] due to invalidation
     * of this [PagingSource]. The [Key] is provided to [load] via [LoadParams.key].
     *
     * The [Key] returned by this method should cause [load] to load enough items to
     * fill the viewport around the last accessed position, allowing the next generation to
     * transparently animate in. The last accessed position can be retrieved via
     * [state.anchorPosition][PagingState.anchorPosition], which is typically
     * the top-most or bottom-most item in the viewport due to access being triggered by binding
     * items as they scroll into view.
     *
     * For example, if items are loaded based on integer position keys, you can return
     * [state.anchorPosition][PagingState.anchorPosition].
     *
     * Alternately, if items contain a key used to load, get the key from the item in the page at
     * index [state.anchorPosition][PagingState.anchorPosition].
     *
     * @param state [PagingState] of the currently fetched data, which includes the most recently
     * accessed position in the list via [PagingState.anchorPosition].
     *
     * @return [Key] passed to [load] after invalidation used for initial load of the next
     * generation. The [Key] returned by [getRefreshKey] should load pages centered around
     * user's current viewport. If the correct [Key] cannot be determined, `null` can be returned
     * to allow [load] decide what default key to use.
     */
    override fun getRefreshKey(state: PagingState<Int, Words>): Int? {
        TODO("Not yet implemented")
    }

    /**
     * Loading API for [PagingSource].
     *
     * Implement this method to trigger your async load (e.g. from database or network).
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Words> {

        return try {
            var pageNumber: Int? = null
            params.key?.let {
                if (it > 1) {
                    pageNumber = it
                }
            }
            val wordResponse = BanglaDictionary.banglaDictionaryApi()
                .getAllWords(Constant.API_KEY, pageNumber, letterId)
            var beforePage: Int? = null
            var nextPage: Int? = null
            if (wordResponse.page < wordResponse.total_pages) {
                nextPage = wordResponse.page + 1
            }

            if (wordResponse.page > 1) {
                beforePage = wordResponse.page - 1
            }

            val page = wordResponse.page

            val words = wordResponse.words

            LoadResult.Page(words ?: listOf(), beforePage, nextPage)
        } catch (exception: IOException) { // 6
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }


    }


}