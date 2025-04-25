package com.example.movietime

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movietime.adapter.MovieAdapter
import com.example.movietime.data.MovieAPIService
import com.example.movietime.data.MovieSearchResponse
import com.example.movietime.data.Movies
import com.example.movietime.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovieAdapter
    private var moviesList: List<Movies> = emptyList() // Cambiar a MutableList

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MovieAdapter(moviesList) { position ->
            navigateToDetail(moviesList[position])
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        searchMoviesByTitle("batman")
    }

    private fun navigateToDetail(movies: Movies) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("MOVIES_ID", movies.imdbID)
        startActivity(intent)
    }

    private fun searchMoviesByTitle(title: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = getRetrofit().create(MovieAPIService::class.java)
                val response: MovieSearchResponse = service.searchMoviesByTitle(title)
                withContext(Dispatchers.Main) {
                    if (response.response == "True") {
                        // Limpiar la lista actual y agregar solo la primera película
                        moviesList = response.movies
                        adapter.items = moviesList
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@MainActivity, "No se encontraron películas", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Configuración de Retrofit
    private val BASE_URL = "https://www.omdbapi.com/"

    private fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}
