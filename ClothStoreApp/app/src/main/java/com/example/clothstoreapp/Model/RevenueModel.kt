package com.example.clothstoreapp.Model

data class RevenueModel(
    val date: String,
    var totalRevenue: Double,
    var orderCount: Int,
    var averageOrderValue: Double = 0.0  // Thêm giá trị trung bình mỗi đơn hàng
)