package com.example.mystoryapp.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mystoryapp.databinding.ActivityDetailStoryBinding
import com.example.mystoryapp.response.ListStoryItem

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var stories: ListStoryItem

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stories = intent.getParcelableExtra("Stories")!!

        stories.let { stories ->
            binding.apply {
                tvDetailStory.text = stories.name
                tvDeskripsiStory.text = stories.description
                Glide.with(ivStory.context).load(stories.photoUrl).into(ivStory)
            }
        }
    }
}