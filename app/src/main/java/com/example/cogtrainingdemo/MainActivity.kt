package com.example.cogtrainingdemo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cogtrainingdemo.data.api.CharactersRemoteDataSource
import com.example.cogtrainingdemo.data.api.RestApi
import com.example.cogtrainingdemo.data.model.CharactersItem
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import com.example.cogtrainingdemo.databinding.ActivityMainBinding
import com.example.cogtrainingdemo.ui.listener.CallbackListener
import com.example.cogtrainingdemo.ui.main.CharactersAdapter
import com.example.cogtrainingdemo.ui.main.intent.MainIntent
import com.example.cogtrainingdemo.ui.main.viewState.MainState
import com.example.cogtrainingdemo.ui.viewModel.CharactersViewModel
import com.example.cogtrainingdemo.ui.views.EpisodeListFragment
import com.example.cogtrainingdemo.ui.views.EposideListFragment_TAG
import com.example.cogtrainingdemo.ui.views.ViewBase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ViewBase, CallbackListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<CharactersViewModel>()

    private val service: CharactersRemoteDataSource = RestApi.getRetrofitInstance()
    lateinit var repository: CharactersRepository
    private val adapter = CharactersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        repository = CharactersRepository.getInstance(service)
        setContentView(binding.root)
        setUpAdapter()
        initViewModel()
        observeViewModelStates()
        sendUserIntent()

        binding.episodeBtn.setOnClickListener {
            showEpisodesList()
        }
    }

    private fun setUpAdapter() {
        adapter.registerCallbackListener(this)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun showEpisodesList(characterName: String? = null) {
        binding.placeHolder.visibility = View.VISIBLE
        binding.mainLayout.visibility = View.GONE
        val episodesFragment = EpisodeListFragment.newInstance(characterName, this)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.placeHolder, episodesFragment, EposideListFragment_TAG)
            .commit()
    }

    private fun showCharacterList() {
        supportFragmentManager.findFragmentByTag(EposideListFragment_TAG)?.let {
            supportFragmentManager.beginTransaction().remove(
                it
            )
        }
        binding.placeHolder.visibility = View.GONE
        binding.mainLayout.visibility = View.VISIBLE
    }

    private fun initViewModel() {
        viewModel.init(this, repository)
    }

    private fun sendUserIntent() {
        lifecycleScope.launch {
            viewModel.userIntent.send(MainIntent.FetchUser)
        }
    }

    override fun observeViewModelStates() {
        lifecycleScope.launch {
            viewModel.charactersState.collect { state ->

                when (state) {
                    is MainState.Idle -> {
//                        binding.progressBar.visibility = View.GONE
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
                        Toast.makeText(this@MainActivity, state.errorMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.unRegisterCallbackListener()
    }

    override fun onBackPress() {
        showCharacterList()
    }

    override fun clickOnItem(item: Any?) {
        if (item is CharactersItem) showEpisodesList(item.name)
    }
}
