package com.example.mymoviesapp

data class MovieTrailerResponse(val results: List<MovieTrailer>)
data class MovieTrailer(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
)

