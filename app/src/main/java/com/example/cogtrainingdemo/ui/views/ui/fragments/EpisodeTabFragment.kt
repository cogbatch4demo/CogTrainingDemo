package com.example.cogtrainingdemo.ui.views.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cogtrainingdemo.R
import com.example.cogtrainingdemo.data.api.CharactersRemoteDataSource
import com.example.cogtrainingdemo.data.api.RestApi
import com.example.cogtrainingdemo.data.model.EpisodesItem
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import com.example.cogtrainingdemo.databinding.FragmentEposideListListBinding
import com.example.cogtrainingdemo.ui.listener.CallbackListener
import com.example.cogtrainingdemo.ui.main.intent.MainIntent
import com.example.cogtrainingdemo.ui.main.viewState.MainState
import com.example.cogtrainingdemo.ui.viewModel.EpisodesViewModel
import com.example.cogtrainingdemo.ui.views.FragmentBaseView
import com.example.cogtrainingdemo.ui.views.RxSearchEpisodeObservable
import com.example.cogtrainingdemo.ui.views.ui.activities.EpisodesDetailScreen
import com.example.cogtrainingdemo.ui.views.ui.adapters.EpisodeItemRecyclerViewAdapter
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class DashboardFragment : FragmentBaseView(),
    CallbackListener {

    private lateinit var binding: FragmentEposideListListBinding
    private val service: CharactersRemoteDataSource = RestApi.getRetrofitInstance()
    lateinit var repository: CharactersRepository
    override val viewModel by viewModels<EpisodesViewModel>()
    private var disposable: Disposable? = null
    private var episodesAdapter = EpisodeItemRecyclerViewAdapter(
        listOf()
    )

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

        binding = FragmentEposideListListBinding.inflate(inflater)
        episodesAdapter.registerCallbackListener(this)
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
            sendUserIntent(null)
        }
        binding.searchView.setOnQueryTextFocusChangeListener{ view, isFocus ->
            if (isFocus) searchEpisodeWithCharacterName()
        }
        return binding.root
    }

    private fun searchEpisodeWithCharacterName() {
        disposable = RxSearchEpisodeObservable.fromView(binding.searchView)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .map { text ->
                text.lowercase().trim()
            }
            .distinctUntilChanged()
            .switchMap { s ->
                Observable.just(s)
            }
            .observeOn(Schedulers.io())
            .subscribe { query ->
                sendUserIntent(query)
            }
    }

    private fun sendUserIntent(characterName: String?) {
        lifecycleScope.launch {
            if (characterName.isNullOrBlank())
                viewModel.userIntent.send(MainIntent.FetchEpisodes)
            else
                viewModel.userIntent.send(MainIntent.GetEpisodesWithCharacter(characterName))
        }
    }

    override fun observeViewModelStates() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.viewState.collect { state ->
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

    override fun clickOnItem(item: Any?) {
        if (item is EpisodesItem) {
            val intent = Intent(requireContext(), EpisodesDetailScreen::class.java)
            intent.putExtra(EpisodesDetailScreen.DATA_CONTENT, item)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        episodesAdapter.unRegisterCallbackListener()
        if (disposable?.isDisposed == false)
            disposable?.dispose()
    }

}