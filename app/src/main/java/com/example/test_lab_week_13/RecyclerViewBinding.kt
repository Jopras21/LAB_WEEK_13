package com.example.test_lab_week_13

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.test_lab_week_13.model.Movie

object RecyclerViewBinding { // <-- Ganti class jadi object
    @JvmStatic // <-- Tambahkan ini agar aman
    @BindingAdapter("list")
    fun bindList(view: RecyclerView, list: List<Movie>?) {
        val adapter = view.adapter as? MovieAdapter
        if (adapter != null && list != null) {
            adapter.addMovies(list)
        }
    }
}