package com.example.clothstoreapp.Adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clothstoreapp.Model.BrandModel
import com.example.clothstoreapp.R
import com.example.clothstoreapp.databinding.ViewholderBrandBinding

class BrandAdapter(
    val items: MutableList<BrandModel>,
    private val onBrandSelected: (BrandModel?) -> Unit  // Chấp nhận null
) : RecyclerView.Adapter<BrandAdapter.Viewholder>() {

    private var selectedPosition = -1
    private lateinit var context: Context

    class Viewholder(val binding: ViewholderBrandBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderBrandBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: BrandAdapter.Viewholder, position: Int) {
        val item = items[position]
        holder.binding.title.text = item.name

        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .into(holder.binding.pic)

        // Kiểm tra trạng thái đã chọn
        if (selectedPosition == position) {
            holder.binding.pic.setBackgroundColor(0)
            holder.binding.mainLayout.setBackgroundResource(R.drawable.grey_bg)
            ImageViewCompat.setImageTintList(holder.binding.pic, ColorStateList.valueOf(context.getColor(R.color.white)))
            holder.binding.title.visibility = View.VISIBLE
        } else {
            holder.binding.pic.setBackgroundResource(R.drawable.grey_bg)
            holder.binding.mainLayout.setBackgroundColor(0)
            ImageViewCompat.setImageTintList(holder.binding.pic, ColorStateList.valueOf(context.getColor(R.color.black)))
            holder.binding.title.visibility = View.GONE
        }

        holder.binding.root.setOnClickListener {
            if (selectedPosition == position) {
                // Nếu item đã chọn được click lại, reset selectedPosition và hiển thị tất cả sản phẩm
                selectedPosition = -1
                Log.d("BrandUnselected", "Unselected Brand: ${item.id}")
                onBrandSelected(null)  // Truyền null khi unselect
            } else {
                selectedPosition = position
                Log.d("BrandSelected", "Selected Brand ID: ${item.id}")
                onBrandSelected(item)  // Truyền brand đã chọn
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = items.size
}

