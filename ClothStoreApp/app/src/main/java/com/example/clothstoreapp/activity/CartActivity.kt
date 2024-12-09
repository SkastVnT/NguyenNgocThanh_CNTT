package com.example.clothstoreapp.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothstoreapp.Adapter.CartAdapter
import com.example.clothstoreapp.R
import com.example.clothstoreapp.databinding.ActivityCartBinding
import com.example.project1762.Helper.ChangeNumberItemsListener
import com.example.project1762.Helper.ManagmentCart

class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managmentCart: ManagmentCart
    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart = ManagmentCart(this)
        setVariable()
        initCartList()
        calculateCart()
    }

    private fun initCartList() {
        binding.viewCart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.viewCart.adapter = CartAdapter(managmentCart.getListCart(), this, object : ChangeNumberItemsListener {
            override fun onChanged() {
                calculateCart()
            }
        })

        with(binding) {
            emptyTxt.visibility = if (managmentCart.getListCart().isEmpty()) View.VISIBLE else View.GONE
            scrollView2.visibility = if (managmentCart.getListCart().isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun calculateCart(): Double {
        val percentTax = 0.02
        val delivery = 10.0
        val itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100.0
        tax = Math.round((itemTotal * percentTax) * 100) / 100.0
        val total = Math.round((itemTotal + tax + delivery) * 100) / 100.0

        with(binding) {
            totalFeetxt.text = "$$itemTotal"
            taxTxt.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }

        return total
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }

        binding.checkoutBtn.setOnClickListener {
            proceedToCheckout()
        }
    }

    private fun proceedToCheckout() {
        if (managmentCart.getListCart().isEmpty()) {
            Toast.makeText(this, "EMPTY LIST", Toast.LENGTH_SHORT).show()
            return
        }

                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("TOTAL_AMOUNT", calculateCart())
                startActivityForResult(intent, PAYMENT_REQUEST_CODE)

    }

    // Nhận kết quả từ PaymentActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PAYMENT_REQUEST_CODE && resultCode == RESULT_OK) {
            val paymentSuccess = data?.getBooleanExtra("PAYMENT_SUCCESS", false) ?: false
            if (paymentSuccess) {
                // Nếu thanh toán thành công, xóa giỏ hàng
                managmentCart.clearCart()
                initCartList() // Cập nhật lại giao diện sau khi xóa giỏ hàng
                calculateCart()


            }
        }
    }

    companion object {
        private const val PAYMENT_REQUEST_CODE = 1001
    }
}

