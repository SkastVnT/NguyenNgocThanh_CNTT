package com.example.clothstoreapp.Model

data class OrderDetailModel(

val productId: String? = null,
val name: String? = null,
val price: Double = 0.0,
val quantity: Int = 0,
val totalPrice: Double = 0.0,

val imageUrl: String? = null
)

