package com.example.mymoviesapp

data class MovieCastResponse(val cast: List<MovieCast>)
data class MovieCast(
    val cast_id: Int,
    val character: String,
    val name: String,
    val profile_path: String
)
