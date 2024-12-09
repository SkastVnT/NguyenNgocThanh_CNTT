package com.example.clothstoreapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clothstoreapp.Model.ItemsModel
import com.example.clothstoreapp.databinding.ItemUpdateProductBinding

class UpdateProductAdapter(
    private var products: MutableList<ItemsModel>,
    private val onEditClick: (ItemsModel) -> Unit
) : RecyclerView.Adapter<UpdateProductAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemUpdateProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUpdateProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]

        holder.binding.apply {
            nameTv.text = product.name
            priceTv.text = "$${product.price}"

            // Load image using Glide
            if (product.images.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(product.images[0])
                    .into(productIv)
            }

            editBtn.setOnClickListener { onEditClick(product) }
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateList(newList: MutableList<ItemsModel>) {
        products = newList
        notifyDataSetChanged()
    }
}