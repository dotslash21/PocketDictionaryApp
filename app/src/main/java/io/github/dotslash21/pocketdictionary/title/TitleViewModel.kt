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

    fun onSearch() {
        if (inputValidation()) {
            _onSearchEvent.value = true
        }
    }

    fun onSearchComplete() {
        _onSearchEvent.value = false
    }

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

    fun onErrorEventHandled() {
        _onErrorEvent.value = null
    }
}