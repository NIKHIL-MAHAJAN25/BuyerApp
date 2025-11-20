package com.nikhil.buyerapp.news

data class NewsResponse(

    val results: List<Result>,
    val status: String,
    val totalResults: Int
)

data class Result(
    val article_id: String,
    val title: String,
    val description: String,
    val image_url: String?,
    val link: String,
    val pubDate: String?,
    val source_name: String,


)