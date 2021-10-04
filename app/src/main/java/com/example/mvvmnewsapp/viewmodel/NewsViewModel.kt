package com.example.mvvmnewsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnewsapp.model.NewsResponse
import com.example.mvvmnewsapp.model.Source
import com.example.mvvmnewsapp.repository.NewsRepository
import com.example.mvvmnewsapp.util.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
):ViewModel() {
    var breakingNewsPageNumber = 1
    val breakingNews:MutableLiveData<Resources<NewsResponse>> = MutableLiveData()

    fun getBreakingNews(countryCode: String){
        viewModelScope.launch(Dispatchers.IO) {
            breakingNews.postValue(Resources.Loading())
            val response = newsRepository.getBreakingNews(countryCode, breakingNewsPageNumber)
            breakingNews.postValue(handleBreakingNewsResponse(response))
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

}