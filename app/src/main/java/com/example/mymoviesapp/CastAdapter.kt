package com.example.mymoviesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mymoviesapp.databinding.ItemCastBinding

class CastAdapter(private val cast: List<MovieCast>) : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(cast[position])
    }

    override fun getItemCount(): Int {
        return cast.size
    }

    inner class CastViewHolder(private val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: MovieCast) {
            binding.castName.text = cast.name
            val profileUrl = "https://image.tmdb.org/t/p/w500" + cast.profile_path
            Glide.with(binding.castImage.context).load(profileUrl).into(binding.castImage)
        }
    }
}
