package com.nikhil.buyerapp.dataclasses

data class Client(
    val uid: String,
    val companyName: String? = null,
    val interested: List<String> = listOf(),
    val budgetRange: Map<String, Double>? = null,
    val paymentMethods: List<String> = emptyList(),
    val rating:Double ?= 0.0,
    )