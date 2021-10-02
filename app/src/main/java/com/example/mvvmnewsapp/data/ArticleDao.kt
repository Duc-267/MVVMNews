package com.example.mvvmnewsapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmnewsapp.model.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticle(article:Article)

    @Query("SELECT * FROM articles_table")
    fun readAllData():LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}