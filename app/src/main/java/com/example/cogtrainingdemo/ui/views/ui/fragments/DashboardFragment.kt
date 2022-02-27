package com.example.cogtrainingdemo.ui.views.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cogtrainingdemo.R
import com.example.cogtrainingdemo.data.api.CharactersRemoteDataSource
import com.example.cogtrainingdemo.data.api.RestApi
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import com.example.cogtrainingdemo.databinding.FragmentDashboardBinding
import com.example.cogtrainingdemo.ui.main.intent.MainIntent
import com.example.cogtrainingdemo.ui.main.viewState.MainState
import com.example.cogtrainingdemo.ui.viewModel.EpisodesViewModel
import com.example.cogtrainingdemo.ui.views.EpisodeItemRecyclerViewAdapter
import com.example.cogtrainingdemo.ui.views.ViewBase
import kotlinx.coroutines.launch

class DashboardFragment: Fragment(), ViewBase {

    private lateinit var binding: FragmentDashboardBinding
    private var characterName: String? = null
    private val viewModel by viewModels<EpisodesViewModel>()
    private val service: CharactersRemoteDataSource = RestApi.getRetrofitInstance()
    lateinit var repository: CharactersRepository
    private var episodesAdapter = EpisodeItemRecyclerViewAdapter(
        listOf()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = CharactersRepository.getInstance(service)
        viewModel.init(requireContext(), repository)

      /*  arguments?.let {
            characterName = it.getString(CHARACTER_NAME, "").uppercase()
        }*/
        observeViewModelStates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // val layout = inflater.inflate(R.layout.fragment_eposide_list_list, container, false)
        binding = FragmentDashboardBinding.inflate(inflater)


        // Set the adapter
        with(binding.recyclerviewEpisodes) {
            LinearLayoutManager(context)
            adapter = episodesAdapter
            val mDividerItemDecoration = DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
            this.context.resources.getDrawable(R.drawable.divider)
                ?.let { mDividerItemDecoration.setDrawable(it) }
            addItemDecoration(mDividerItemDecoration)
            sendUserIntent()
        }
        return binding.root
    }

    private fun sendUserIntent() {
        lifecycleScope.launch {
            if (characterName.isNullOrBlank())
                viewModel.userIntent.send(MainIntent.FetchEpisodes)
            else
                viewModel.userIntent.send(MainIntent.GetEpisodesWithCharacter(characterName))
        }
    }

   /* companion object {
        const val CHARACTER_NAME = "character_name"

        @JvmStatic
        fun newInstance(characterName: String?, callbackListener: CallbackListener) =
            EpisodeListFragment(callbackListener).apply {
                arguments = Bundle().apply {
                    putString(CHARACTER_NAME, characterName)
                }
            }
    }*/

    override fun observeViewModelStates() {
        lifecycleScope.launch {
            viewModel.eposidesState.collect { state ->
                when (state) {
                    is MainState.Idle -> {
                    }
                    is MainState.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                        binding.recyclerviewEpisodes.visibility = View.GONE
                    }
                    is MainState.Episodes -> {
                        binding.progressbar.visibility = View.GONE
                        binding.recyclerviewEpisodes.visibility = View.VISIBLE
                        episodesAdapter.updateEpisodeList(state.data)
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
}