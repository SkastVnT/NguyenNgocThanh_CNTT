package com.example.clothstoreapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clothstoreapp.activity.MainActivity
import com.example.clothstoreapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.continueBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                handleLogin(email, password)
            } else {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.move.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        database.child("users").child(it.uid).get()
                            .addOnSuccessListener { snapshot ->
                                val userRole = snapshot.child("role").getValue(String::class.java) ?: "customer"
                                val intent = Intent(this, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    putExtra("USER_ROLE", userRole)
                                }
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Lỗi khi lấy dữ liệu user role", e)
                                Toast.makeText(this, "Không thể lấy dữ liệu user role.", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            database.child("users").child(currentUser.uid).get()
                .addOnSuccessListener { snapshot ->
                    val userRole = snapshot.child("role").getValue(String::class.java) ?: "customer"
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        putExtra("USER_ROLE", userRole)
                    }
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Log.e(TAG, "Không thể kiểm tra user role")
                }
        }
    }
}
