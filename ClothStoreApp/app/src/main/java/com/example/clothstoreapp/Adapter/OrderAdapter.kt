package com.example.clothstoreapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.clothstoreapp.Model.OrderModel
import com.example.clothstoreapp.R
import com.example.clothstoreapp.activity.OrderDetailActivity

class OrderAdapter(
    private var orders: MutableList<OrderModel>, // Sử dụng MutableList để có thể cập nhật danh sách
    private val context: Context
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: TextView = view.findViewById(R.id.orderIdTxt)
        val totalAmount: TextView = view.findViewById(R.id.totalAmountTxt)
        val viewDetailBtn: Button = view.findViewById(R.id.viewDetailBtn)
        val orderDateTxt: TextView = view.findViewById(R.id.orderDateTxt)
        val orderTimeTxt: TextView = view.findViewById(R.id.orderTimeTxt)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderId.text = "Order ID: ${order.orderId}"
        holder.totalAmount.text = "Total: $${order.totalAmount}"
        holder.orderDateTxt.text = order.orderDate
        holder.orderTimeTxt.text = order.orderTime

        holder.viewDetailBtn.setOnClickListener {
            try {
                val intent = Intent(holder.itemView.context, OrderDetailActivity::class.java)
                intent.putExtra("orderId", order.orderId)
                holder.itemView.context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(holder.itemView.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = orders.size

    // Thêm phương thức cập nhật danh sách đơn hàng
    fun updateOrders(newOrders: List<OrderModel>) {
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }
}
