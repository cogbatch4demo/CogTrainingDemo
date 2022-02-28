package com.example.cogtrainingdemo.ui.views

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import com.example.cogtrainingdemo.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


object RxSearchEpisodeObservable {

    fun hideKeyboard(searchView: SearchView) {
        val imm =
            searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.getApplicationWindowToken(), 0)
        searchView.clearFocus()
    }

    fun fromView(searchView: SearchView): Observable<String> {
        val subject: PublishSubject<String> = PublishSubject.create()
        searchView.queryHint = searchView.context.resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard(searchView)
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                subject.onNext(newText)
                return true
            }
        })
        searchView.setOnCloseListener {
            hideKeyboard(searchView)
            subject.onNext("")
            subject.onComplete()
            true
        }
        return subject
    }
}