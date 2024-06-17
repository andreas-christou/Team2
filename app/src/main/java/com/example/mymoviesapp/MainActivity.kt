package com.example.mymoviesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymoviesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MyPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the ViewPager with an adapter
        adapter = MyPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter

        // Link the TabLayout and the ViewPager
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }
}


