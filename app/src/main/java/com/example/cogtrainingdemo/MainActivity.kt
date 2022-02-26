package com.example.cogtrainingdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.cogtrainingdemo.databinding.ActivityMainBinding
import com.example.cogtrainingdemo.ui.listener.CallbackListener
import com.example.cogtrainingdemo.ui.views.*


class MainActivity : AppCompatActivity(), ViewBase, CallbackListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var charactersListFragment: CharactersListFragment
    private lateinit var episodeListFragment: EpisodeListFragment
    override val viewModel: ViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        charactersListFragment = CharactersListFragment()
        episodeListFragment = EpisodeListFragment.newInstance(null)
        setContentView(binding.root)
        observeViewModelStates()
        setUpUi()
        setCurrentFragment(charactersListFragment, CharactersListFragment_TAG)
    }

    private fun setUpUi() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.characterTab -> setCurrentFragment(
                    charactersListFragment,
                    CharactersListFragment_TAG
                )
                R.id.episodeTab -> setCurrentFragment(episodeListFragment, EposideListFragment_TAG)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment, tag: String) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.placeHolder, fragment, tag)
            commit()
        }

    override fun observeViewModelStates() {
    }

    override fun onBackPress() {
    }

    override fun clickOnItem(item: Any?) {

    }
}
