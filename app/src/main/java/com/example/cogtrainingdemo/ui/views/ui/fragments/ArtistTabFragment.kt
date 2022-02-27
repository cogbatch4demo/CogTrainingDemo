package com.example.cogtrainingdemo.ui.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cogtrainingdemo.CHARACTER_DATA
import com.example.cogtrainingdemo.data.api.CharactersRemoteDataSource
import com.example.cogtrainingdemo.data.api.RestApi
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import com.example.cogtrainingdemo.databinding.FragmentArtistsBinding
import com.example.cogtrainingdemo.ui.main.CharactersAdapter
import com.example.cogtrainingdemo.ui.main.intent.MainIntent
import com.example.cogtrainingdemo.ui.main.viewState.MainState
import com.example.cogtrainingdemo.ui.viewModel.CharactersViewModel
import com.example.cogtrainingdemo.ui.views.ui.activities.DetailsScreen
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentArtistsBinding? = null
    private val viewModel by viewModels<CharactersViewModel>()
    private val adapter = CharactersAdapter()
    private val binding get() = _binding!!
    lateinit var repository: CharactersRepository
    private val service: CharactersRemoteDataSource = RestApi.getRetrofitInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

          repository = CharactersRepository.getInstance(service)

        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
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
        adapter.onItemClick = {character ->
            val bundle = Bundle()
            bundle.putSerializable(CHARACTER_DATA,character)
            val intent = Intent(requireContext(), DetailsScreen::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    fun observeViewModelStates() {
        lifecycleScope.launch {
            viewModel.charactersState.collect { state ->

                when (state) {
                    is MainState.Idle -> {

                    }
                    is MainState.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainState.Characters -> {
//                        binding.progressBar.visibility = View.GONE
//                        binding.recyclerview.visibility = View.VISIBLE
                        adapter.updateCharacters(state.data)
                    }
                    is MainState.Error -> {
                        Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}