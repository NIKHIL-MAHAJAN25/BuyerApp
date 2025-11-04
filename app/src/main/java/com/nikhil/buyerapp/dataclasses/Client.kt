package com.nikhil.buyerapp.dataclasses

data class Client(
    val uid: String,
    val name:String="",
    val state:String="",
    val companyName: String? = null,
    val interested: List<String> = listOf(),
    val minBudget: Double? = null,
    val maxBudget: Double? = null,
    val paymentMethods: List<String> = emptyList(),
    val rating:Double ?= 0.0,
    val profcomp:Boolean?=false
    )