package com.example.cogtrainingdemo.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cogtrainingdemo.data.api.CharactersRemoteDataSource
import com.example.cogtrainingdemo.data.api.RestApi
import com.example.cogtrainingdemo.data.model.CharactersItem
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import com.example.cogtrainingdemo.databinding.FragmentCharactersListListBinding
import com.example.cogtrainingdemo.ui.listener.CallbackListener
import com.example.cogtrainingdemo.ui.main.CharactersAdapter
import com.example.cogtrainingdemo.ui.main.intent.MainIntent
import com.example.cogtrainingdemo.ui.main.viewState.MainState
import com.example.cogtrainingdemo.ui.viewModel.CharactersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
const val CharactersListFragment_TAG = "CharactersListFragment"

class CharactersListFragment : Fragment(), ViewBase,
    CallbackListener {


    private lateinit var binding: FragmentCharactersListListBinding
    override val viewModel by viewModels<CharactersViewModel>()
    private val service: CharactersRemoteDataSource = RestApi.getRetrofitInstance()
    lateinit var repository: CharactersRepository
    private var charactersAdapter = CharactersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = CharactersRepository.getInstance(service)
        viewModel.init(requireContext(), repository)
        observeViewModelStates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // val layout = inflater.inflate(R.layout.fragment_eposide_list_list, container, false)
        binding = FragmentCharactersListListBinding.inflate(inflater)

        charactersAdapter.registerCallbackListener(this)
        // Set the adapter
        with(binding.recyclerview) {
            LinearLayoutManager(context)
            adapter = charactersAdapter
            sendUserIntent()
        }
        return binding.root
    }

    private fun sendUserIntent() {
        lifecycleScope.launch {
            viewModel.userIntent.send(MainIntent.FetchUser)
        }
    }

    override fun observeViewModelStates() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.charactersState.collect { state ->
                when (state) {
                    is MainState.Idle -> {
                    }
                    is MainState.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                        binding.recyclerview.visibility = View.VISIBLE
                    }
                    is MainState.Characters -> {
                        binding.progressbar.visibility = View.GONE
                        binding.recyclerview.visibility = View.VISIBLE
                        charactersAdapter.updateCharacters(state.data)

                    }
                    is MainState.Error -> {
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                    else -> Unit

                }

            }
        }
    }

    override fun onBackPress() {
    }

    override fun clickOnItem(item: Any?) {
        if (item is CharactersItem) {
            Toast.makeText(requireContext(), "Show the detail character screen!", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        charactersAdapter.unRegisterCallbackListener()
    }
}