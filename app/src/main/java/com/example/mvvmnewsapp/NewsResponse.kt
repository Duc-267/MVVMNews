package com.example.mvvmnewsapp

import com.example.mvvmnewsapp.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)