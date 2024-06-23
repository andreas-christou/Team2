package com.example.mymoviesapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymoviesapp.databinding.FragmentFavoritesBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gson: Gson
    private lateinit var adapter: MovieAdapter
    private var favorites: MutableList<Movie> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE)
        gson = Gson()


        binding.btnClearFavorites.setOnClickListener {
            clearFavorites()
        }

        // Retrieve the favorites from SharedPreferences
        loadFavorites()

        // Set up the RecyclerView with the adapter
        adapter = MovieAdapter(requireContext(), mutableListOf(), false,{ movie ->
            // Handle click on movie if needed
        }) // Hide the button in FavoritesFragment



        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        // Update the favorites list when the fragment resumes
        loadFavorites()
        adapter.movies = favorites
        adapter.notifyDataSetChanged()
    }

    private fun clearFavorites() {
        val editor = sharedPreferences.edit()
        editor.remove("favorites_list")
        editor.apply()

        // Clear the list in the adapter and update RecyclerView
        adapter.clearMovies()
    }

    private fun loadFavorites() {
        val json = sharedPreferences.getString("favorites_list", null)
        val type = object : TypeToken<MutableList<Movie>>() {}.type
        favorites = gson.fromJson(json, type) ?: mutableListOf()
    }
}
