package com.example.cogtrainingdemo.ui.views

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class FragmentBaseView: Fragment() {
    abstract fun observeViewModelStates()
    open val viewModel: ViewModel? = null
}