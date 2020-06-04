package io.github.dotslash21.pocketdictionary.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class SearchResultViewModelFactory(private val searchWord: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchResultViewModel::class.java)) {
            return SearchResultViewModel(searchWord) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}