package com.example.clothstoreapp.activity.admin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothstoreapp.Adapter.UpdateProductAdapter
import com.example.clothstoreapp.Model.ItemsModel
import com.example.clothstoreapp.R
import com.example.clothstoreapp.databinding.ActivityUpdateProductBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UpdateProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProductBinding
    private lateinit var adapter: UpdateProductAdapter
    private lateinit var productsList: MutableList<ItemsModel>
    private val database = FirebaseDatabase.getInstance()
    private var valueEventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSearch()
        loadProducts()
    }

    override fun onResume() {
        super.onResume()
        loadProducts() // Reload data when returning to this activity
    }

    override fun onPause() {
        super.onPause()
        // Remove listener when activity is paused
        valueEventListener?.let {
            database.getReference("products").removeEventListener(it)
        }
    }
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Update Products"
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupRecyclerView() {
        productsList = mutableListOf()
        adapter = UpdateProductAdapter(productsList) { product ->
            // Handle edit button click
            showEditDialog(product)
        }
        binding.productsRv.layoutManager = LinearLayoutManager(this)
        binding.productsRv.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterProducts(s.toString())
            }
        })
    }

    private fun loadProducts() {
        valueEventListener = database.getReference("products")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productsList.clear()
                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(ItemsModel::class.java)
                        product?.let {
                            it.productId = productSnapshot.key ?: ""
                            productsList.add(it)
                        }
                    }
                    adapter.updateList(productsList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@UpdateProductActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun filterProducts(query: String) {
        val filteredList = if (query.isEmpty()) {
            productsList
        } else {
            productsList.filter { it.name.contains(query, ignoreCase = true) }
        }
        adapter.updateList(filteredList.toMutableList())
    }

    private fun showEditDialog(product: ItemsModel) {
        val intent = Intent(this, EditProductActivity::class.java)
        intent.putExtra("product", product)
        startActivity(intent)
    }
}