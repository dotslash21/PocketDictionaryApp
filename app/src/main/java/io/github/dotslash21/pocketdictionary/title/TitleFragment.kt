package io.github.dotslash21.pocketdictionary.title

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import io.github.dotslash21.pocketdictionary.R
import io.github.dotslash21.pocketdictionary.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    private lateinit var viewModel: TitleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(
            inflater,
            R.layout.fragment_title,
            container,
            false
        )

        // Enable the options menu
        setHasOptionsMenu(true)

        // Get the view model
        viewModel = ViewModelProviders.of(this).get(TitleViewModel::class.java)

        // Connect the layout with viewModel
        binding.titleViewModel = viewModel

        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        // Observe when user clicks Done on keyboard
        binding.searchEditText.setOnEditorActionListener(fun(
            _: TextView,
            actionId: Int,
            event: KeyEvent?
        ): Boolean {
            if ((event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                binding.searchButton.performClick()
            }
            return false
        })

        // Observe when user clicks the search button
        viewModel.onSearchEvent.observe(this, Observer { event ->
            if (event) {
                findNavController()
                    .navigate(
                        TitleFragmentDirections.actionTitleFragmentToSearchResultFragment(
                            viewModel.searchWord.value!!
                        )
                    )
                viewModel.onSearchComplete()
            }
        })

        // Observe for any error events
        viewModel.onErrorEvent.observe(this, Observer { event ->
            if (event != null) {
                Toast.makeText(
                    this.context,
                    event.errMessage,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.onErrorEventHandled()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item!!,
            view!!.findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}
