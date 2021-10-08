package com.example.mvvmnewsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewsapp.model.Article
import com.example.mvvmnewsapp.model.NewsResponse
import com.example.mvvmnewsapp.repository.NewsRepository
import com.example.mvvmnewsapp.util.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
):ViewModel() {
    private var breakingNewsPageNumber = 1
    val breakingNews:MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    private var searchNewsPageNumber = 1
    val searchNews:MutableLiveData<Resources<NewsResponse>> = MutableLiveData()

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode: String){
        viewModelScope.launch(Dispatchers.IO) {
            breakingNews.postValue(Resources.Loading())
            val response = newsRepository.getBreakingNews(countryCode, breakingNewsPageNumber)
            breakingNews.postValue(handleBreakingNewsResponse(response))
        }
    }

    fun getSearchNews(searchQuery: String){
        viewModelScope.launch(Dispatchers.IO) {
            searchNews.postValue(Resources.Loading())
            val response = newsRepository.getSearchNews(searchQuery, searchNewsPageNumber)
            searchNews.postValue(handleSearchNewsResponse(response))
        }
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resources<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resourcesResponse ->
                return Resources.Success(resourcesResponse)
            }
        }
        return Resources.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resources<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resourcesResponse ->
                return Resources.Success(resourcesResponse)
            }
        }
        return Resources.Error(response.message())
    }

    fun addSavedNews(article: Article){
        viewModelScope.launch {
            newsRepository.addToDataBase(article)
        }
    }

    fun getSavedNews()=
        newsRepository.getAllSavedNews()


    fun deleteSavedNews(article: Article){
        viewModelScope.launch {
            newsRepository.deleteSavedNews(article)
        }
    }


}