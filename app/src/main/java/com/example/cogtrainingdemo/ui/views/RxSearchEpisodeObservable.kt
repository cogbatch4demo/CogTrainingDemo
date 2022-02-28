package com.example.cogtrainingdemo.ui.views

import androidx.appcompat.widget.SearchView
import com.example.cogtrainingdemo.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxSearchEpisodeObservable {

    fun fromView(searchView: SearchView): Observable<String> {
        val subject: PublishSubject<String> = PublishSubject.create()
        searchView.queryHint = searchView.context.resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                println("Huong onQueryTextSubmit $query")
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                subject.onNext(newText)
                println("Huong $newText")
                return true
            }
        })
        return subject
    }
}