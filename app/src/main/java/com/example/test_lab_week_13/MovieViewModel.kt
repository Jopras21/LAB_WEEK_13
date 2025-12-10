package com.example.test_lab_week_13

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository)
    : ViewModel() {
    init {
        fetchPopularMovies()
    }
    // define the LiveData
    private val _popularMovies = MutableStateFlow(
        emptyList<Movie>()
    )
    val popularMovies: StateFlow<List<Movie>> = _popularMovies
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error
    // fetch movies from the API
    private fun fetchPopularMovies() {
// launch a coroutine in viewModelScope
// Dispatchers.IO means that this coroutine will run on a shared pool of threads
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.fetchMovies()
                .map { movieList ->
                    // Kita mencegat list film, lalu mengurutkannya
                    // dari popularity terbesar ke terkecil (Descending)
                    movieList.sortedByDescending { movie ->
                        movie.popularity
                    }
                }
                .catch {
                    // catch is a terminal operator that catches exceptions from the Flow
                    _error.value = "An exception occurred: ${it.message}"
                }.collect { sortedMovies ->
                    sortedMovies.take(5).forEach { movie ->
                        Log.d("CEK_SORTING", "Judul: ${movie.title} | Popularity: ${movie.popularity}")
                    }
                    // collect is a terminal operator that collects the values from the Flow
                    // Data yang masuk ke sini (sortedMovies) sudah terurut rapi
                    _popularMovies.value = sortedMovies
                }
        }
    }
}