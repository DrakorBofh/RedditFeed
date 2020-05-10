package com.fristano.redditfeedchallenge.view.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.fristano.redditfeedchallenge.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {

    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }
}