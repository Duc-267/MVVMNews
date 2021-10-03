package com.example.mvvmnewsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mvvmnewsapp.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
):ViewModel() {
}