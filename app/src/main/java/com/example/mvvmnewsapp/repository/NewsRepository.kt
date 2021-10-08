package com.example.mvvmnewsapp.repository

import com.example.mvvmnewsapp.api.RetrofitInstance
import com.example.mvvmnewsapp.data.ArticleDatabase
import com.example.mvvmnewsapp.model.Article
import com.example.mvvmnewsapp.model.NewsResponse
import retrofit2.Response

class NewsRepository(
    val db : ArticleDatabase
){
    suspend fun getBreakingNews(countryCode: String, pageNumber:Int): Response<NewsResponse> {
        return RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
    }

    suspend fun getSearchNews(searchQuery: String, pageNumber:Int): Response<NewsResponse> {
        return RetrofitInstance.api.searchNews(searchQuery, pageNumber)
    }

    suspend fun addToDataBase(article: Article){
        db.articleDao().addArticle(article)
    }

    fun getAllSavedNews()=
        db.articleDao().readAllData()


    suspend fun deleteSavedNews(article: Article){
        db.articleDao().deleteArticle(article)
    }
}