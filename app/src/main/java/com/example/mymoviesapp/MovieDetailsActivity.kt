package com.example.mymoviesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mymoviesapp.databinding.ActivityMovieDetailsBinding
import android.content.Intent
import android.net.Uri

import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var tmdbService: TMDBService
    private val apiKey = "7ac19f7f2937e84e3fb99cbefb642866"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie: Movie? = intent.getParcelableExtra("movie")
        movie?.let {
            binding.movieTitle.text = it.title
            binding.movieReleaseDate.text = it.release_date
            binding.movieRating.text = it.vote_average.toString()
            binding.movieOverview.text = it.overview
            val posterUrl = "https://image.tmdb.org/t/p/w500" + it.poster_path
            Glide.with(this).load(posterUrl).into(binding.moviePoster)
            loadTrailer(it.id)
            loadCast(it.id)
        }

        binding.goBackButton.setOnClickListener {
            finish()
        }
    }

    private fun loadTrailer(movieId: Int) {
        tmdbService = RetrofitInstance.getRetrofitInstance().create(TMDBService::class.java)
        tmdbService.getMovieTrailers(movieId, apiKey).enqueue(object : Callback<MovieTrailerResponse> {
            override fun onResponse(call: Call<MovieTrailerResponse>, response: Response<MovieTrailerResponse>) {
                val trailers = response.body()?.results
                if (!trailers.isNullOrEmpty()) {
                    val trailer = trailers[0]
                    if (trailer.site == "YouTube") {
                        val youtubeThumbnailUrl = "https://img.youtube.com/vi/${trailer.key}/0.jpg"
                        Glide.with(this@MovieDetailsActivity).load(youtubeThumbnailUrl).into(binding.movieTrailer)
                        binding.movieTrailer.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${trailer.key}"))
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MovieTrailerResponse>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun loadCast(movieId: Int) {
        tmdbService.getMovieCast(movieId, apiKey).enqueue(object : Callback<MovieCastResponse> {
            override fun onResponse(call: Call<MovieCastResponse>, response: Response<MovieCastResponse>) {
                val cast = response.body()?.cast
                cast?.let {
                    val castAdapter = CastAdapter(it)
                    binding.castRecyclerView.layoutManager = LinearLayoutManager(this@MovieDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
                    binding.castRecyclerView.adapter = castAdapter
                }
            }

            override fun onFailure(call: Call<MovieCastResponse>, t: Throwable) {
                // Handle error
            }
        })
    }
}
