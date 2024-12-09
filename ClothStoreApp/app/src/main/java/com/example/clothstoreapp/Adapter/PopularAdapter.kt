package com.example.clothstoreapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.clothstoreapp.Model.ItemsModel
import com.example.clothstoreapp.activity.DetailActivity
import com.example.clothstoreapp.databinding.ViewholderRecommendedBinding

class PopularAdapter(val items: MutableList<ItemsModel>) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    private var context: Context? = null

    class ViewHolder(val binding: ViewholderRecommendedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.ViewHolder {
        context = parent.context
        val binding = ViewholderRecommendedBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularAdapter.ViewHolder, position: Int) {
        val item = items[position]

        // Set tên và giá
        holder.binding.titleTxt.text = item.name
        val price = item.price
        val formattedPrice = if (price % 1 == 0.0) {
            "$${price.toInt()}"
        } else {
            "$${price}"
        }
        holder.binding.priceTxt.text = formattedPrice

        // Hiển thị hình ảnh
        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(item.images[0])
            .apply(requestOptions)
            .into(holder.binding.pic)

        // Kiểm tra tồn kho
        if (item.stock > 0) {
            holder.itemView.isEnabled = true
            holder.itemView.alpha = 1f
        } else {
                holder.binding.stockStatusTxt.text = "HẾT HÀNG"
            holder.binding.stockStatusTxt.setTextColor(context!!.getColor(android.R.color.holo_red_dark))
            holder.itemView.isEnabled = false
            holder.itemView.alpha = 0.5f // Làm mờ để biểu thị sản phẩm bị vô hiệu hóa
        }

        // Xử lý sự kiện click khi còn hàng
        holder.itemView.setOnClickListener {
            if (item.stock > 0) {
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra("object", item)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun sortByPriceAscending() {
        items.sortBy { it.price }
        notifyDataSetChanged()
    }

    fun sortByPriceDescending() {
        items.sortByDescending { it.price }
        notifyDataSetChanged()
    }
}

