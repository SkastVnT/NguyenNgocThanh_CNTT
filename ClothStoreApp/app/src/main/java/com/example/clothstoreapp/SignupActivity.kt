package com.example.clothstoreapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clothstoreapp.Model.UserModel
import com.example.clothstoreapp.activity.MainActivity
import com.example.clothstoreapp.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        binding.continueBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val fullName = binding.fullname.text.toString().trim()
            val address = binding.address.text.toString().trim()
            val phone = binding.phone.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || fullName.isEmpty() ||
                address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.let {
                            val userInfo = UserModel(
                                email = email,
                                fullName = fullName,
                                address = address,
                                phone = phone,
                                role = "customer"  // Mặc định là customer khi đăng ký
                            )

                            database.child("users").child(it.uid).setValue(userInfo)
                                .addOnSuccessListener {
                                    Toast.makeText(baseContext, "Sign up successful",
                                        Toast.LENGTH_SHORT).show()
                                    // Chuyển đến MainActivity với role
                                    val intent = Intent(this, MainActivity::class.java).apply {
                                        putExtra("USER_ROLE", "customer")
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(baseContext, "Error: ${e.message}",
                                        Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.move.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Kiểm tra role của user hiện tại
            database.child("users").child(currentUser.uid).get()
                .addOnSuccessListener { snapshot ->
                    val userRole = snapshot.child("role").getValue(String::class.java) ?: "customer"
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("USER_ROLE", userRole)
                    }
                    startActivity(intent)
                    finish()
                }
        }
    }
}
