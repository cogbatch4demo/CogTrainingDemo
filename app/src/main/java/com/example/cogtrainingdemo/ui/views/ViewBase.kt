package com.example.cogtrainingdemo.ui.views

import androidx.lifecycle.ViewModel

interface ViewBase {
    fun observeViewModelStates()
    val viewModel: ViewModel
}