package io.github.dotslash21.pocketdictionary.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import io.github.dotslash21.pocketdictionary.WordDefinitionsAdapter
import io.github.dotslash21.pocketdictionary.databinding.FragmentSearchResultBinding

class SearchResultFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchResultBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Get args using by navArgs property delegate
        val searchResultFragmentArgs by navArgs<SearchResultFragmentArgs>()

        // Get the viewModel
        val viewModelFactory = SearchResultViewModelFactory(searchResultFragmentArgs.searchWord)
        val viewModel: SearchResultViewModel by lazy {
            ViewModelProviders.of(this, viewModelFactory).get(SearchResultViewModel::class.java)
        }

        // Connect viewModel with layout
        binding.searchResultViewModel = viewModel

        // Tell meaningsRecyclerView about MeaningsAdapter.
        val adapter = WordDefinitionsAdapter()
        binding.wordDefinitionsRecyclerView.adapter = adapter

        viewModel.definitions.observe(this, Observer {
            it?.let {
                adapter.data = it
            }
        })

        return binding.root
    }
}
