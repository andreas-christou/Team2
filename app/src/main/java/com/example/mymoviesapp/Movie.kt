package com.example.mymoviesapp

data class MovieResponse(
    val results: List<Movie>
)

data class Movie(
    val title: String,
    val poster_path: String
)

