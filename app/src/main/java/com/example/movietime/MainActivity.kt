package com.example.movietime

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
    private var moviesList: List<Movies> = emptyList()

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

    // Aquí ya no usamos RetrofitProvider, sino que obtenemos Retrofit directamente
    private fun findMovieById(imdbID: String) {
        val service: MovieAPIService = getRetrofit().create(MovieAPIService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val movie = service.findMovieById(imdbID)
                withContext(Dispatchers.Main) {
                    moviesList = listOf(movie)
                    adapter.updateData(moviesList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun searchMoviesByTitle(title: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = getRetrofit().create(MovieAPIService::class.java)
                val response: MovieSearchResponse = service.searchMoviesByTitle(title)
                withContext(Dispatchers.Main) {
                    if (response.response == "True") {
                        moviesList = response.movies
                        adapter.updateData(moviesList)
                    } else {
                        Toast.makeText(this@MainActivity, "No se encontraron películas", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MovieAdapter(moviesList) { position ->
            navigateToDetail(moviesList[position])
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        findMovieById("tt3896198")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        initSearchView(menu?.findItem(R.id.search))
        return true
    }

    private fun initSearchView(searchItem: MenuItem?) {
        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        searchMoviesByTitle(it)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun navigateToDetail(movies: Movies) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("MOVIES_ID", movies.imdbID)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
