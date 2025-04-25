package com.example.movietime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movietime.data.Movies
import com.example.movietime.databinding.ItemMovieBinding


class Movieadapter(var items: List<Movies>, val onClick: (Int) -> Unit) : Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movies = items[position]
        holder.render(movies)
        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }
}

class MovieViewHolder(val binding: ItemMovieBinding) : ViewHolder(binding.root) {

    fun render(movies: Movies) {
        binding.movieTextView.text = movies.Title
    }
}