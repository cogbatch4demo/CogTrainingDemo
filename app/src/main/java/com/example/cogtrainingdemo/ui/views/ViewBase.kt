package com.example.cogtrainingdemo.ui.views

import androidx.lifecycle.ViewModel

interface ViewBase {
    val viewModel: ViewModel?
    fun observeViewModelStates()
}