package com.example.movietime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movietime.data.Movies
import com.example.movietime.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso


class MovieAdapter(
    private var dataSet: List<Movies> = mutableListOf(),
    private val onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviesViewHolder(binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.render(dataSet[position])
        holder.itemView.setOnClickListener {
            onItemClickListener(holder.adapterPosition)
        }
    }

    // Esta función actualizará la lista y notificará los cambios
    fun updateData(newDataSet: List<Movies>) {
        notifyDataSetChanged() // Notificar el cambio en la lista completa
    }

    // ViewHolder interno para el RecyclerView
    class MoviesViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun render(movies: Movies) {
            binding.titleTextView.text = movies.title
            Picasso.get().load(movies.imageURL).into(binding.posterImageView)
        }
    }
}

