package com.example.mymoviesapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoviesapp.databinding.FragmentFirstBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MovieAdapter
    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false
    private lateinit var favorites: MutableList<Movie>
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter(requireContext(), mutableListOf() ,true) { movie ->
            val fragment = MovieDetailsFragment.newInstance(movie)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = movieAdapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadMoreMovies()
                    }
                }
            }
        })

        loadMoreMovies()
        loadFavorites()
    }

    private fun loadMoreMovies() {
        isLoading = true
        val apiKey = "7ac19f7f2937e84e3fb99cbefb642866"
        RetrofitInstance.api.getPopularMovies(apiKey, page = currentPage).enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results ?: emptyList()
                    movieAdapter.addMovies(movies)
                    currentPage++
                    isLoading = false
                    isLastPage = movies.isEmpty()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                // Handle failure
                isLoading = false
            }
        })
    }

    private fun addToFavorites(movie: Movie) {
        if (!favorites.contains(movie)) {
            favorites.add(movie)
            saveFavorites()
        }
    }

    private fun loadFavorites() {
        val sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("favorites_list", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Movie>>() {}.type
            favorites = gson.fromJson(json, type)
        } else {
            favorites = mutableListOf()
        }
    }

    private fun saveFavorites() {
        val sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = gson.toJson(favorites)
        editor.putString("favorites_list", json)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
