package com.example.a12bookreview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.a12bookreview.databinding.ActivityDetailBinding
import com.example.a12bookreview.model.Book

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model = intent.getParcelableExtra<Book>("bookModel")
        binding.titleTextView.text = model?.title
        binding.descriptionTextView.text = model?.description
        Glide.with(this).load(model?.coverSmallUrl).into(binding.coverImageView)
    }
}