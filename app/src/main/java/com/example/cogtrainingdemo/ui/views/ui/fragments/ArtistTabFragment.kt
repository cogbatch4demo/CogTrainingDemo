package com.example.cogtrainingdemo.ui.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cogtrainingdemo.CHARACTER_DATA
import com.example.cogtrainingdemo.data.api.CharactersRemoteDataSource
import com.example.cogtrainingdemo.data.api.RestApi
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import com.example.cogtrainingdemo.databinding.FragmentArtistsBinding
import com.example.cogtrainingdemo.ui.views.ui.adapters.CharactersAdapter
import com.example.cogtrainingdemo.ui.main.intent.MainIntent
import com.example.cogtrainingdemo.ui.main.viewState.MainState
import com.example.cogtrainingdemo.ui.viewModel.CharactersViewModel
import com.example.cogtrainingdemo.ui.views.FragmentBaseView
import com.example.cogtrainingdemo.ui.views.ui.activities.DetailsScreen
import kotlinx.coroutines.launch


class HomeFragment : FragmentBaseView() {

    private lateinit var binding: FragmentArtistsBinding
    override val viewModel by viewModels<CharactersViewModel>()
    private val adapter = CharactersAdapter()
    lateinit var repository: CharactersRepository
    private val service: CharactersRemoteDataSource = RestApi.getRetrofitInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        repository = CharactersRepository.getInstance(service)

        binding = FragmentArtistsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpAdapter()
        initViewModel()
        observeViewModelStates()
        sendUserIntent()
        return root
    }

    private fun setUpAdapter() {
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter.onItemClick = { character ->
            val bundle = Bundle()
            bundle.putSerializable(CHARACTER_DATA, character)
            val intent = Intent(requireContext(), DetailsScreen::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun observeViewModelStates() {
        lifecycleScope.launch {
            viewModel.viewState.collect { state ->

                when (state) {
                    is MainState.Idle -> {

                    }
                    is MainState.Loading -> {
                        binding.spinner.visibility = VISIBLE
                        binding.recyclerview.visibility = GONE
                    }
                    is MainState.Characters -> {
                        binding.spinner.visibility = GONE
                        binding.recyclerview.visibility = VISIBLE
                        adapter.updateCharacters(state.data)
                    }
                    is MainState.Error -> {
                        Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit
                }

            }
        }
    }

    private fun sendUserIntent() {
        lifecycleScope.launch {
            viewModel.userIntent.send(MainIntent.FetchUser)
        }
    }

    private fun initViewModel() {
        context?.let { viewModel.init(it, repository) }
    }

}
