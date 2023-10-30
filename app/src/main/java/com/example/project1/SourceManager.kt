package com.example.project1

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject


class SourceManager {
    val okHttpClient: OkHttpClient

    init{
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        okHttpClient = builder.build()
    }

    fun retrieveSources(category: String, apiKey: String): List<SourcesData>{
        val request = Request.Builder()
            .url("https://newsapi.org/v2/top-headlines/sources?country=us&category=$category&apiKey=$apiKey")
            .header(
                "Authorization",
                "Bearer $apiKey"
            )
            .get()
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()
            val sourceData = mutableListOf<SourcesData>()
            if (response.isSuccessful && ! responseBody.isNullOrEmpty() ){
                val json = JSONObject(responseBody)
                val sources = json.getJSONArray("sources")

                // parse json response
                for (i in 0 until sources.length()){
                    val currentSource = sources.getJSONObject(i)
                    val sourceName = currentSource.getString("name")
                    val sourceDescription = currentSource.getString("description")
                    val sourceId = currentSource.getString("id")
                    val source = SourcesData(sourceName=sourceName, sourceDescription=sourceDescription, "","","","",id = sourceId)
                    sourceData.add(source)
                }
            }
            return sourceData
        } catch (exception: Exception){
            Log.e("Source Manager", "retrieveSources error with okHTTPClient Request: $exception")
        }

        return listOf()
    }

    fun retrieveArticles(query: String, apiKey: String): List<SourcesData>{
        val request = Request.Builder()
            .url("https://newsapi.org/v2/everything?searchin=title&language=en&q=$query&apiKey=$apiKey")
            .header(
                "Authorization",
                "Bearer $apiKey"
            )
            .get()
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()
            val articleData = mutableListOf<SourcesData>()
            if (response.isSuccessful && ! responseBody.isNullOrEmpty() ){
//            Log.e("SM", responseBody)
                val json = JSONObject(responseBody)
                val articles = json.getJSONArray("articles")

                // parse json response
                for (i in 0 until articles.length()){
                    val currentArticle = articles.getJSONObject(i)
                    val currentSource = currentArticle.getJSONObject("source")
                    val sourceName = currentSource.getString("name")
                    val articleTitle = currentArticle.getString("title")
                    val articleContent = currentArticle.getString("content")
                    val articleImage = currentArticle.getString("urlToImage")
                    val articleURL = currentArticle.getString("url")
                    val articleId = currentSource.getString("id")
                    val source = SourcesData(sourceName=sourceName, sourceDescription="", articleTitle,articleContent,articleImage,articleURL, articleId)
                    Log.d("SM", source.toString())
                    articleData.add(source)
                }
            }

            return articleData
        } catch (exception: Exception){
            Log.e("Source Manager", "retrieveArticles error with okHTTPClient Request: $exception")
        }

        return listOf()
    }


    fun retrieveArticlesWithSource(query: String, source: String, apiKey: String): List<SourcesData>{
        val request = Request.Builder()
            .url("https://newsapi.org/v2/everything?sources=$source&searchin=title&language=en&q=$query&apiKey=$apiKey")
            .header(
                "Authorization",
                "Bearer $apiKey"
            )
            .get()
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()
            val articleData = mutableListOf<SourcesData>()
            if (response.isSuccessful && ! responseBody.isNullOrEmpty() ){
//            Log.e("SM", responseBody)
                val json = JSONObject(responseBody)
                val articles = json.getJSONArray("articles")

                // parse json response
                for (i in 0 until articles.length()){
                    val currentArticle = articles.getJSONObject(i)
                    val currentSource = currentArticle.getJSONObject("source")
                    val sourceName = currentSource.getString("name")
                    val articleTitle = currentArticle.getString("title")
                    val articleContent = currentArticle.getString("content")
                    val articleImage = currentArticle.getString("urlToImage")
                    val articleURL = currentArticle.getString("url")
                    val articleId = currentSource.getString("id")
                    val data = SourcesData(sourceName=sourceName, sourceDescription="", articleTitle,articleContent,articleImage,articleURL, articleId)
                    Log.d("SM", data.toString())
                    articleData.add(data)
                }
            }
            return articleData
        } catch (exception: Exception){
            Log.e("Source Manager", "retrieveArticlesWithSource error with okHTTPClient Request: $exception")
        }

        return listOf()
    }

    fun retrieveTopHeadlines(category: String, pageNumber: Int, apiKey: String): List<SourcesData>{
        val request = Request.Builder()
            .url("https://newsapi.org/v2/top-headlines?country=us&page=$pageNumber&category=$category&apiKey=$apiKey")
            .header(
                "Authorization",
                "Bearer $apiKey"
            )
            .get()
            .build()
        try {
            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()
            val articleData = mutableListOf<SourcesData>()
            if (response.isSuccessful && ! responseBody.isNullOrEmpty() ){
//            Log.e("SM", responseBody)
                val json = JSONObject(responseBody)
                val articles = json.getJSONArray("articles")

                // parse json response
                for (i in 0 until articles.length()){
                    val currentArticle = articles.getJSONObject(i)
                    val currentSource = currentArticle.getJSONObject("source")
                    val sourceName = currentSource.getString("name")
                    val articleTitle = currentArticle.getString("title")
                    val articleContent = currentArticle.getString("content")
                    val articleImage = currentArticle.getString("urlToImage")
                    val articleURL = currentArticle.getString("url")
                    val articleId = currentSource.getString("id")
                    val source = SourcesData(sourceName=sourceName, sourceDescription="", articleTitle,articleContent,articleImage,articleURL, articleId)
                    Log.d("SM", source.toString())
                    articleData.add(source)
                }
            }
            return articleData
        } catch (exception: Exception){
            Log.e("Source Manager", "retrieveTopHeadlines error with okHTTPClient Request: $exception")
        }
        return listOf()
    }
}
