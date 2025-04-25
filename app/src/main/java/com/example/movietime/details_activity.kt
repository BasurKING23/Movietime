package com.example.movietime

import android.graphics.Movie
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movietime.data.MovieAPIService
import com.example.movietime.data.Movies
import com.example.movietime.databinding.DetailsActivityBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {

    companion object {
        const val MOVIES_ID = "MOVIES_ID"
    }

    private lateinit var movies: Movies
    private lateinit var binding: DetailsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imdbID = intent.getStringExtra(MOVIES_ID) ?: return

        findMoviesById(imdbID)
    }

    private fun findMoviesById(imdbID: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")  // Asegúrate de que esta es la URL base correcta
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: MovieAPIService = retrofit.create(MovieAPIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val movie = service.findMovieById(imdbID)  // Llamada a la API para obtener los detalles de la película
                withContext(Dispatchers.Main) {
                    movies = movie
                    loadData(movie)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadData(movies: Movies) {
        try {
            binding.apply {
                titleTextView.text = movies.title
                genreTextView.text = movies.genre
                yearTextView.text = movies.year
                runtimeTextView.text = movies.runtime
                directorTextView.text = movies.director
                countryTextView.text = movies.country
                plotTextView.text = movies.plot

                Picasso.get().load(movies.imageURL).into(posterImageView)  // Carga la imagen de la película
            }
        } catch (e: NullPointerException) {
            Toast.makeText(this, "Error loading movie data", Toast.LENGTH_SHORT).show()
            finish()  // Cierra la actividad si ocurre un error al cargar los datos
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()  // Cierra la actividad cuando se hace clic en la flecha de regreso
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


