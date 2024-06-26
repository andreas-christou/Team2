package com.example.mymoviesapp

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymoviesapp.databinding.ItemMovieBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import androidx.appcompat.app.AlertDialog
import android.content.Intent

class MovieAdapter(
    private val context: Context,
    public var movies: MutableList<Movie>,
    private val showAddToFavoritesButton: Boolean,
    private val clickListener: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position], clickListener, showAddToFavoritesButton)
    }

    override fun getItemCount() = movies.size

    fun addMovies(newMovies: List<Movie>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    fun clearMovies() {
        movies.clear()
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, clickListener: (Movie) -> Unit, showAddToFavoritesButton: Boolean) {
            binding.movieTitle.text = movie.title
            val posterUrl = "https://image.tmdb.org/t/p/w500" + movie.poster_path
            Glide.with(binding.moviePoster.context).load(posterUrl).into(binding.moviePoster)
            binding.root.setOnClickListener {
                clickListener(movie)
            }

            // Set movie details
            binding.movieReleaseDate.text = "Release Date: ${movie.release_date}"
            binding.movieRating.text = "Rating: ${movie.vote_average}"
            binding.movieOverview.text = movie.overview

            // Initially hide the details
            binding.movieReleaseDate.visibility = View.GONE
            binding.movieRating.visibility = View.GONE
            binding.movieOverview.visibility = View.GONE

            // Handle "Details" button click
            binding.btnDetails.setOnClickListener {
                if (binding.movieReleaseDate.visibility == View.GONE) {
                    binding.movieReleaseDate.visibility = View.VISIBLE
                    binding.movieRating.visibility = View.VISIBLE
                    binding.movieOverview.visibility = View.VISIBLE
                    binding.btnDetails.text = "Hide Summary"
                } else {
                    binding.movieReleaseDate.visibility = View.GONE
                    binding.movieRating.visibility = View.GONE
                    binding.movieOverview.visibility = View.GONE
                    binding.btnDetails.text = "Summary"
                }
            }

            if (showAddToFavoritesButton) {
                binding.btnAddToFavorites.visibility = View.VISIBLE
                binding.btnRmvToFavorites.visibility = View.GONE
                binding.btnAddToFavorites.setOnClickListener {
                    saveToFavorites(movie)
                }
            } else {
                binding.btnAddToFavorites.visibility = View.GONE
                binding.btnRmvToFavorites.visibility = View.VISIBLE
            }
            binding.btnRmvToFavorites.setOnClickListener {
                removeFromFavorites(movie)
            }

            binding.btnDetails2.setOnClickListener {
                val intent = Intent(context, MovieDetailsActivity::class.java)
                intent.putExtra("movie", movie)
                context.startActivity(intent)
            }

        }

        }

        private fun saveToFavorites(movie: Movie) {
            val editor = sharedPreferences.edit()
            val json = sharedPreferences.getString("favorites_list", null)
            val type = object : TypeToken<MutableList<Movie>>() {}.type
            val favorites: MutableList<Movie> = gson.fromJson(json, type) ?: mutableListOf()
            if (!favorites.contains(movie)) {
                favorites.add(movie)
                val updatedJson = gson.toJson(favorites)
                editor.putString("favorites_list", updatedJson)
                editor.apply()
                Log.d("Favorites", "Movie added to favorites: ${movie.title}")
            } else {
                Log.d("Favorites", "Movie already in favorites: ${movie.title}")
                showAlertAlreadyInFavorites()
            }
        }
    private fun showAlertAlreadyInFavorites() {
        AlertDialog.Builder(context)
            .setTitle("Already in Favorites")
            .setMessage("This movie is already in your favorites list.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun removeFromFavorites(movie: Movie) {
        val editor = sharedPreferences.edit()
        val json = sharedPreferences.getString("favorites_list", null)
        val type = object : TypeToken<MutableList<Movie>>() {}.type
        val favorites: MutableList<Movie> = gson.fromJson(json, type) ?: mutableListOf()
        if (favorites.contains(movie)) {
            favorites.remove(movie)
            val updatedJson = gson.toJson(favorites)
            editor.putString("favorites_list", updatedJson)
            editor.apply()
            Log.d("Favorites", "Movie removed from favorites: ${movie.title}")
            movies.remove(movie)
            notifyDataSetChanged()
        } else {
            Log.d("Favorites", "Movie not found in favorites: ${movie.title}")
        }
    }

}
