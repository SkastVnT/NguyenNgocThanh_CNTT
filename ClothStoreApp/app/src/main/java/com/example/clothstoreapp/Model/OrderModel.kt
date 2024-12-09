package com.example.clothstoreapp.Model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class OrderModel(
    val orderId: String? = "",
    val fullName: String = "",
    val phone: String = "",
    val address: String = "",
    val email: String? = "",
    val paymentMethod: String = "COD",
    val orderDetails: List<OrderDetailModel> = listOf(),
    val  totalAmount: Double = 0.0,
    var timestamp: Long = System.currentTimeMillis(),
    val orderDate: String = getCurrentDate(), // Ngày đặt hàng
    val orderTime: String = getCurrentTime()  // Giờ đặt hàng
)

// Thêm các hàm để lấy ngày và giờ hiện tại
private fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date())
}

private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}
