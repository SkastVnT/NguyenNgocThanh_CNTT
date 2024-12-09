package com.example.clothstoreapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clothstoreapp.Model.OrderModel
import com.example.clothstoreapp.Model.RevenueModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RevenueViewModel : ViewModel() {
    private val _revenueData = MutableLiveData<List<RevenueModel>>()
    val revenueData: LiveData<List<RevenueModel>> = _revenueData


    private var startDate: Date? = null
    private var endDate: Date? = null



    init {
        // Set mặc định là 30 ngày gần nhất
        val calendar = Calendar.getInstance()
        endDate = calendar.time
        calendar.add(Calendar.DAY_OF_MONTH, -30)
        startDate = calendar.time

        loadRevenueData()
    }

    fun setStartDate(date: Date) {
        startDate = date
        loadRevenueData()
    }

    fun setEndDate(date: Date) {
        endDate = date
        loadRevenueData()
    }

    private fun loadRevenueData() {
        val ordersRef = FirebaseDatabase.getInstance().getReference("Orders")

        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val revenueMap = mutableMapOf<String, RevenueModel>()

                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(OrderModel::class.java)
                    order?.let {
                        if (isOrderInDateRange(it)) {
                            val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(order.timestamp)

                            val revenue = revenueMap.getOrPut(dateStr) {
                                RevenueModel(dateStr, 0.0, 0)
                            }

                            revenue.totalRevenue += order.totalAmount
                            revenue.orderCount++
                        }
                    }
                }

                _revenueData.value = revenueMap.values.toList()
                    .sortedBy { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(it.date) }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi
            }
        })
    }

    private fun isOrderInDateRange(order: OrderModel): Boolean {
        return (startDate == null || order.timestamp >= startDate!!.time) &&
                (endDate == null || order.timestamp <= endDate!!.time)
    }
}