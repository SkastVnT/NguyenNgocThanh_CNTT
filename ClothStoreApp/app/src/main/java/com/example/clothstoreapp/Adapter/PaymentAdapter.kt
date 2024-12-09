// CheckoutAdapter.kt
package com.example.clothstoreapp.Adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.clothstoreapp.Model.ItemsModel
import com.example.clothstoreapp.databinding.ViewholderCheckoutBinding

class PaymentAdapter(
    private val checkoutItems: List<ItemsModel>,
    private val context: Context
) : RecyclerView.Adapter<PaymentAdapter.CheckoutViewHolder>() {

    inner class CheckoutViewHolder(val binding: ViewholderCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val binding = ViewholderCheckoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return CheckoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val item = checkoutItems[position]
        with(holder.binding) {
            checkoutTitleTxt.text = item.name
            checkoutTitleTxt.maxLines = 2
            checkoutTitleTxt.ellipsize = TextUtils.TruncateAt.END
            checkoutFeeEachItem.text = "$${item.price}"
            checkoutQuantityTxt.text =   "Quantity: ${item.numberInCart}"

            val requestOptions = RequestOptions().transform(CenterCrop())
            Glide.with(holder.itemView.context)
                .load(item.images[0])
                .apply(requestOptions)
                .into(checkoutPic)
        }
    }




    override fun getItemCount(): Int = checkoutItems.size
}
