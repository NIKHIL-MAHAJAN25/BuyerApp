package com.nikhil.buyerapp.skills

data class SkillsCat(
    val categoryName: String,
    val skills: List<String>
){
    constructor() : this("", emptyList())
}
