package com.example.clothstoreapp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothstoreapp.Adapter.OrderAdapter
import com.example.clothstoreapp.Model.OrderModel
import com.example.clothstoreapp.databinding.ActivityOrderBinding
import com.example.clothstoreapp.ViewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private val viewModel = MainViewModel()
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setVariable()
        setupRecyclerView()
        observeOrders()
    }

    private fun setupRecyclerView() {
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(this)
        orderAdapter = OrderAdapter(mutableListOf(), this)
        binding.ordersRecyclerView.adapter = orderAdapter
    }

    private fun observeOrders() {
        viewModel.orders.observe(this) { orders ->
            // Sắp xếp đơn hàng từ mới nhất đến cũ nhất
            val sortedOrders = orders.sortedByDescending { order ->
                parseDateTime(order.orderDate, order.orderTime)
            }

            if (sortedOrders.isEmpty()) {
                binding.emptyOrderText.visibility = View.VISIBLE
                binding.ordersRecyclerView.visibility = View.GONE
            } else {
                binding.emptyOrderText.visibility = View.GONE
                binding.ordersRecyclerView.visibility = View.VISIBLE
                orderAdapter.updateOrders(sortedOrders)
            }
        }
        viewModel.loadOrders()
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }

    // Hàm hỗ trợ chuyển đổi ngày và giờ để so sánh
    private fun parseDateTime(date: String, time: String): Long {
        return try {
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val dateTime = formatter.parse("$date $time")
            dateTime?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
}