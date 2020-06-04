package io.github.dotslash21.pocketdictionary.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.dotslash21.pocketdictionary.api.DictionaryApi
import io.github.dotslash21.pocketdictionary.api.WordDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class DictionaryApiStatus { LOADING, ERROR, DONE }

class SearchResultViewModel(searchWord: String) : ViewModel() {
    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<DictionaryApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<DictionaryApiStatus>
        get() = _status

    // The internal MutableLiveData String that stores the most recent response
    private val _definitions = MutableLiveData<List<WordDefinition>>()

    // The external immutable LiveData for the response String
    val definitions: LiveData<List<WordDefinition>>
        get() = _definitions

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Call getWordDefinitions() on init so we can display status immediately.
     */
    init {
        getWordDefinitions(searchWord)
    }

    /**
     * Sets the value of the response LiveData to the Dictionary API status or the raw json string
     */
    private fun getWordDefinitions(searchWord: String) {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            val getDefinitionsDeferred = DictionaryApi.retrofitService.getDefinitions(searchWord)

            try {
                _status.value = DictionaryApiStatus.LOADING

                // Await the completion of our Retrofit request and assign it to _definitions
                val listResult = getDefinitionsDeferred.await()
                _definitions.value = listResult

                _status.value = DictionaryApiStatus.DONE
            } catch (e: Exception) {
                _definitions.value = ArrayList()

                _status.value = DictionaryApiStatus.ERROR
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}