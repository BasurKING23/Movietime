package com.example.movietime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movietime.data.Movies
import com.example.movietime.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso


class MovieAdapter(
    private var items: List<Movies> = mutableListOf(),
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val superhero = items[position]
        holder.render(superhero)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    // ViewHolder interno para el RecyclerView
    class MoviesViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun render(movies: Movies) {
            binding.titleTextView.text = movies.title
            binding.yearTextView.text = movies.year
            Picasso.get().load(movies.imageURL).into(binding.posterImageView)
        }
    }
}

