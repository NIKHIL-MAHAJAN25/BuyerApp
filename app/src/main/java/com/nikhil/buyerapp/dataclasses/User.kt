package com.nikhil.buyerapp.dataclasses



class User (
    val uid: String?=null,
    val email: String?=null,
    val fullName: String?=null,
    val phoneNumber: String?=null,
    val profilePictureUrl: String?=null,
    val state: String?=null,
    val bio: String?=null,
    val occupation:String?=null,
    val language:List<String> = listOf(),
    val categoriesInterestedIn: List<String> = listOf(),
    val accountType: String = "client"
)