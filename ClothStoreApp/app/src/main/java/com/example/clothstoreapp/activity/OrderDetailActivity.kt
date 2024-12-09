package com.example.clothstoreapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothstoreapp.Adapter.OrderDetailAdapter
import com.example.clothstoreapp.Model.OrderModel
import com.example.clothstoreapp.databinding.ActivityOrderDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("orderId")
        loadOrderDetails(orderId)
        setVariable()
    }

    private fun displayOrderDetails(order: OrderModel) {
        binding.apply {
            // Basic order information
            tvOrderId.text = " ${order.orderId}"
            tvCustomerName.text = " ${order.fullName}"
            tvPhone.text = " ${order.phone}"
            tvAddress.text = " ${order.address}"

            // Calculate fees correctly
            val percentTax = 0.02  // 2% tax rate
            val delivery = 10.0    // Fixed delivery fee

            // Calculate itemTotal directly from order details
            val itemTotal = order.orderDetails.sumOf { it.price * it.quantity }
            val tax = itemTotal * percentTax
            val totalAmount = itemTotal + tax + delivery

            // Display fees with proper formatting
            tvItemTotal.text = "$${String.format("%.2f", itemTotal)}"
            tvTax.text = "$${String.format("%.2f", tax)}"
            tvDelivery.text = "$${String.format("%.2f", delivery)}"
            tvTotalAmount.text = "$${String.format("%.2f", totalAmount)}"

            // Set up RecyclerView for order items
            if (!order.orderDetails.isNullOrEmpty()) {
                val adapter = OrderDetailAdapter(order.orderDetails)
                rvOrderItems.layoutManager = LinearLayoutManager(this@OrderDetailActivity)
                rvOrderItems.adapter = adapter
            } else {
                Toast.makeText(this@OrderDetailActivity, "No order details found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadOrderDetails(orderId: String?) {
        if (orderId != null) {
            val orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId)
            orderRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val order = snapshot.getValue(OrderModel::class.java)
                    if (order != null) {
                        displayOrderDetails(order)
                    } else {
                        Toast.makeText(this@OrderDetailActivity, "Order not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@OrderDetailActivity, "Error loading order details", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Invalid Order ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setVariable() {
        binding.btnBack.setOnClickListener { finish() }
    }
}
