package com.example.clothstoreapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clothstoreapp.Model.OrderDetailModel
import com.example.clothstoreapp.R

class OrderDetailAdapter(private val items: List<OrderDetailModel>) :
    RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvQuantity: TextView = view.findViewById(R.id.tvQuantity)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvImage: ImageView = view.findViewById(R.id.tvImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            tvProductName.text = item.name
            tvQuantity.text = "x${item.quantity}"
            tvPrice.text = "$${item.totalPrice}"

            Glide.with(holder.itemView.context)
                .load(item.imageUrl ?: R.drawable.cloth)
                .into(tvImage)
        }
    }

    override fun getItemCount() = items.size
}
