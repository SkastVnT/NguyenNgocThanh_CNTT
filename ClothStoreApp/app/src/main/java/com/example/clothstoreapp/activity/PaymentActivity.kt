package com.example.clothstoreapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothstoreapp.Adapter.PaymentAdapter
import com.example.clothstoreapp.Model.OrderDetailModel
import com.example.clothstoreapp.Model.OrderModel
import com.example.clothstoreapp.Model.UserModel // Thêm import UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseAuth
import com.example.clothstoreapp.R
import com.example.clothstoreapp.databinding.ActivityPaymentBinding
import com.example.project1762.Helper.ManagmentCart

class PaymentActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnConfirmPayment: Button
    private lateinit var totalAmountTxt: TextView
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        etFullName = findViewById(R.id.etFullName)
        etPhone = findViewById(R.id.etPhone)
        etAddress = findViewById(R.id.etAddress)
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment)
        totalAmountTxt = findViewById(R.id.totalAmountTxt)

        // Load user info
        loadUserInfo()

        setVariable()

        val totalAmount = intent.getDoubleExtra("TOTAL_AMOUNT", 0.0)
        totalAmountTxt.text = "$$totalAmount"

        setupCheckoutRecyclerView()

        btnConfirmPayment.setOnClickListener {
            confirmPayment(totalAmount)
            val intent = Intent(this, OrderActivity::class.java)
            intent.putExtra("PAYMENT_SUCCESS", true)
            setResult(RESULT_OK, intent)
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserInfo() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.reference.child("users").child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(UserModel::class.java)
                        user?.let {
                            // Tự động điền thông tin user
                            etFullName.setText(it.fullName)
                            etPhone.setText(it.phone)
                            etAddress.setText(it.address)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@PaymentActivity,
                            "Error loading user info",
                            Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun confirmPayment(totalAmount: Double) {
        val fullName = etFullName.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val address = etAddress.text.toString().trim()
        val email = auth.currentUser?.email

        if (fullName.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Cập nhật thông tin user nếu có thay đổi
        updateUserInfo(fullName, phone, address)

        val database = FirebaseDatabase.getInstance().getReference("Orders")
        val orderId = database.push().key

        if (orderId == null) {
            Toast.makeText(this, "Failed to generate order ID", Toast.LENGTH_SHORT).show()
            return
        }

        val cartItems = ManagmentCart(this).getListCart().map { item ->
            OrderDetailModel(
                productId = item.productId,
                name = item.name,
                price = item.price,
                quantity = item.numberInCart,
                totalPrice = item.price * item.numberInCart,
                imageUrl = item.images[0]
            )
        }

        val order = OrderModel(
            orderId = orderId,
            fullName = fullName,
            phone = phone,
            address = address,
            email = email,
            orderDetails = cartItems,
            totalAmount = totalAmount,
            timestamp = System.currentTimeMillis()
        )

        database.child(orderId).setValue(order)
            .addOnSuccessListener {
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                // Xóa giỏ hàng sau khi đặt hàng thành công
                ManagmentCart(this).clearCart()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo(fullName: String, phone: String, address: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = database.reference.child("users").child(userId)
            val updates = hashMapOf<String, Any>(
                "fullName" to fullName,
                "phone" to phone,
                "address" to address
            )
            userRef.updateChildren(updates)
        }
    }

    private fun setupCheckoutRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewCartItems.layoutManager = layoutManager
        val checkoutItems = ManagmentCart(this).getListCart()
        val adapter = PaymentAdapter(checkoutItems, this)
        binding.recyclerViewCartItems.adapter = adapter
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }
}