package com.example.cogtrainingdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cogtrainingdemo.data.api.CharactersRemoteDataSource
import com.example.cogtrainingdemo.data.api.RestApi
import com.example.cogtrainingdemo.data.repository.CharactersRepository
import com.example.cogtrainingdemo.databinding.ActivityMainBinding
import com.example.cogtrainingdemo.ui.main.CharactersAdapter
import com.example.cogtrainingdemo.ui.main.intent.MainIntent
import com.example.cogtrainingdemo.ui.main.viewState.MainState
import com.example.cogtrainingdemo.ui.viewModel.CharactersViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<CharactersViewModel>()

    private val service: CharactersRemoteDataSource = RestApi.getRetrofitInstance()
    lateinit var repository: CharactersRepository
    private val adapter = CharactersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        repository = CharactersRepository(service)
        setContentView(binding.root)
        setUpAdapter()
        initViewModel()
        observeViewModelStates()
        sendUserIntent()
    }

    private fun setUpAdapter() {
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
    }


    private fun initViewModel() {
        viewModel.init(this, repository)
    }

    private fun sendUserIntent() {
        lifecycleScope.launch {
            viewModel.userIntent.send(MainIntent.FetchUser)
        }
    }

    private fun observeViewModelStates() {
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
}
