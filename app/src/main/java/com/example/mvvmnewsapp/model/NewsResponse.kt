package com.example.mvvmnewsapp.model

import com.example.mvvmnewsapp.model.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)