package io.github.dotslash21.pocketdictionary.title

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class ErrorType(val errMessage: String) {
    INPUT_NOT_A_WORD("Error: Input contains multiple words!"),
    INPUT_IS_EMPTY("Error: Input is blank!")
}

class TitleViewModel : ViewModel() {
    // Public MutableLiveData for 2 way binding with search edit text field.
    var searchWord = MutableLiveData<String>()

    private var _onSearchEvent = MutableLiveData<Boolean>()
    val onSearchEvent: LiveData<Boolean>
        get() = _onSearchEvent

    private var _onErrorEvent = MutableLiveData<ErrorType>()
    val onErrorEvent: LiveData<ErrorType>
        get() = _onErrorEvent

    /**
     * Function to trigger the search event.
     */
    fun onSearch() {
        if (inputValidation()) {
            _onSearchEvent.value = true
        }
    }

    /**
     * Function to turn off the search event if the required task has been performed.
     */
    fun onSearchComplete() {
        _onSearchEvent.value = false
    }

    /**
     * Function to validate the searchEditText string and trigger error event.
     */
    private fun inputValidation(): Boolean {
        // Check if input is empty
        if (searchWord.value == null || searchWord.value!!.isEmpty()) {
            // Set the error
            _onErrorEvent.value = ErrorType.INPUT_IS_EMPTY
            return false
        }

        // Trim the string
        searchWord.value = searchWord.value!!.trim()

        // Check input has multiple words
        if (searchWord.value!!.indexOf(' ') != -1) {
            // Set the error
            _onErrorEvent.value = ErrorType.INPUT_NOT_A_WORD
            return false
        }

        return true
    }

    /**
     * Function to turn off the error event if the required task has been performed.
     */
    fun onErrorEventHandled() {
        _onErrorEvent.value = null
    }
}