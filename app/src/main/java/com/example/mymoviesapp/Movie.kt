package com.example.mymoviesapp
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
data class MovieResponse(
    val results: List<Movie>
)






@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Float,
    val overview: String
) : Parcelable
