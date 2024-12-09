package com.example.clothstoreapp.activity.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.clothstoreapp.databinding.ActivityAdminManagementBinding
import com.google.firebase.database.FirebaseDatabase

class AdminManagementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminManagementBinding
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

    setupUI()
        setupToolbar()
    }
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    private fun setupUI() {
        binding.btnAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
// }
        binding.btnUpdateProduct.setOnClickListener {
            startActivity(Intent(this, UpdateProductActivity::class.java))
        }
//
        binding.btnRevenue.setOnClickListener {
            startActivity(Intent(this, RevenueActivity::class.java))
        }
//        binding.btnManageUsers.setOnClickListener {
//            startActivity(Intent(this, ManageUsersActivity::class.java))
//        }
//    }
} }