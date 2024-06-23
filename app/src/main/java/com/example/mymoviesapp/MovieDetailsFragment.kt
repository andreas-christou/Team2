package com.example.mymoviesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mymoviesapp.databinding.FragmentMovieDetailsBinding



class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            val movie = args.getParcelable<Movie>(ARG_MOVIE)
            movie?.let {
                displayMovieDetails(it)
            }
        }
    }

    private fun displayMovieDetails(movie: Movie) {
        binding.movieTitleTextView.text = movie.title
        binding.movieReleaseDateTextView.text = movie.release_date
        binding.movieRatingTextView.text = movie.vote_average.toString()
        // Load movie poster using Glide or any other image loading library
        val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"
        Glide.with(binding.moviePosterImageView.context).load(posterUrl).into(binding.moviePosterImageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_MOVIE = "arg_movie"

        fun newInstance(movie: Movie): MovieDetailsFragment {
            val fragment = MovieDetailsFragment()
            val args = Bundle()
            args.putParcelable(ARG_MOVIE, movie)
            fragment.arguments = args
            return fragment
        }
    }
}
