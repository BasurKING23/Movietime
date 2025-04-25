package com.example.movietime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movietime.data.Movies
import com.example.movietime.data.OmdbApiService
import com.example.movietime.databinding.DetailsActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    lateinit var binding: DetailsActivityBinding

    lateinit var movies: Movies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = DetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setSupportActionBar(binding.toolbar)
//        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getStringExtra("SUPERHERO_ID")!!
        getMoviesById(id)

    fun loadData() {
        Picasso.get().load(movies.Poster).into(binding.posterImageView)

    fun getRetrofit(): OmdbApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.omdbapi.com/?apikey=cdfa154c&")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(SuperheroService::class.java)
    }

    fun getMoviesById(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = getRetrofit()
                movies = service.getMovieById(id)

                CoroutineScope(Dispatchers.Main).launch {
                    loadData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
