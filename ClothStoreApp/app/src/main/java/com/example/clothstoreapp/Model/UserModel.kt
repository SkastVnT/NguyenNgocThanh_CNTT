package com.example.clothstoreapp.Model

data class UserModel(
    val email: String = "",
    val fullName: String = "",
    val address: String = "",
    val phone: String = "",
    val role: String = "customer",
    val avatarUrl: String = ""
)