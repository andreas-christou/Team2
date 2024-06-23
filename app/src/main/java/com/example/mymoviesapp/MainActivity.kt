package com.example.mymoviesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoviesapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FirstFragment())
                .commit()
        }

        val tabTitles = arrayOf("Popular", "Search", "Favorites")
        binding.viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}
