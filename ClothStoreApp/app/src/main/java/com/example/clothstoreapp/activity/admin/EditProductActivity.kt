package com.example.clothstoreapp.activity.admin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothstoreapp.Adapter.ProductImagesAdapter
import com.example.clothstoreapp.Model.BrandModel
import com.example.clothstoreapp.Model.ItemsModel
import com.example.clothstoreapp.R
import com.example.clothstoreapp.databinding.ActivityEditProductBinding
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class EditProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProductBinding
    private lateinit var product: ItemsModel
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val imagesList = mutableListOf<String>()
    private lateinit var imagesAdapter: ProductImagesAdapter
    private val categories = mutableListOf<BrandModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        product = intent.getParcelableExtra("product")!!
        setupToolbar()
        setupViews()
        loadCategories()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Product"
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupViews() {
        // Set existing product data
        binding.apply {
            nameEt.setText(product.name)
            descriptionEt.setText(product.description)
            priceEt.setText(product.price.toString())
            stockEt.setText(product.stock.toString())

            // Setup images RecyclerView
            imagesRv.layoutManager = LinearLayoutManager(
                this@EditProductActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            imagesList.clear() // Xóa nếu còn dữ liệu cũ
            imagesList.addAll(product.images.toMutableList()) // Chuyển đổi ArrayList sang MutableList
            imagesAdapter = ProductImagesAdapter(imagesList) { position ->
                removeImage(position)
            }
            imagesRv.adapter = imagesAdapter

            // Setup colors
            setupColors()

            // Setup sizes
            setupSizes()
        }
    }

    private fun setupColors() {
        binding.colorsChipGroup.removeAllViews()
        product.colors.forEach { color ->
            addColorChip(color)
        }
    }

    private fun setupSizes() {
        binding.sizesChipGroup.removeAllViews()
        product.sizes.forEach { size ->
            addSizeChip(size)
        }
    }

    private fun loadCategories() {
        database.getReference("categories")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    categories.clear()
                    snapshot.children.forEach { categorySnapshot ->
                        val category = categorySnapshot.getValue(BrandModel::class.java)
                        category?.let {
                            it.id = categorySnapshot.key ?: ""
                            categories.add(it)
                        }
                    }
                    setupCategorySpinner()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@EditProductActivity, "Error loading categories", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories.map { it.name }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        // Set selected category
        val categoryIndex = categories.indexOfFirst { it.id == product.categoryId }
        if (categoryIndex != -1) {
            binding.categorySpinner.setSelection(categoryIndex)
        }
    }

    private fun setupListeners() {
        binding.addColorBtn.setOnClickListener {
            showColorPickerDialog()
        }

        binding.addSizeBtn.setOnClickListener {
            showAddSizeDialog()
        }

        binding.addImageBtn.setOnClickListener {
            pickImage()
        }

        binding.saveBtn.setOnClickListener {
            saveChanges()
        }
    }

    private fun showColorPickerDialog() {
        val dialog = AlertDialog.Builder(this)
        val input = EditText(this)
        input.hint = "Enter color (e.g., Red, Blue, etc.)"
        dialog.setView(input)
        dialog.setPositiveButton("Add") { _, _ ->
            val color = input.text.toString()
            if (color.isNotEmpty()) {
                product.colors.add(color)
                addColorChip(color)
            }
        }
        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }

    private fun showAddSizeDialog() {
        val dialog = AlertDialog.Builder(this)
        val input = EditText(this)
        input.hint = "Enter size (e.g., S, M, L, XL)"
        dialog.setView(input)
        dialog.setPositiveButton("Add") { _, _ ->
            val size = input.text.toString()
            if (size.isNotEmpty()) {
                product.sizes.add(size)
                addSizeChip(size)
            }
        }
        dialog.setNegativeButton("Cancel", null)
        dialog.show()
    }

    private fun addColorChip(color: String) {
        val chip = Chip(this)
        chip.text = color
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            product.colors.remove(color)
            binding.colorsChipGroup.removeView(chip)
        }
        binding.colorsChipGroup.addView(chip)
    }


    private fun addSizeChip(size: String) {
        val chip = Chip(this)
        chip.text = size
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            product.sizes.remove(size)
            setupSizes() // Làm mới lại giao diện sizes
        }
        binding.sizesChipGroup.addView(chip)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                uploadImage(uri)
            }
        }
    }

    private fun removeImage(position: Int) {
        // Xóa ảnh khỏi danh sách
        imagesList.removeAt(position)
        imagesAdapter.notifyItemRemoved(position)

        // Đồng bộ lại danh sách ảnh vào product.images
        product.images.clear()
        product.images.addAll(imagesList)
    }

    private fun uploadImage(uri: Uri) {
        val imageRef = storage.reference.child("products/${product.productId}/${System.currentTimeMillis()}")
        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Thêm ảnh mới vào danh sách
                    val imageUrl = downloadUrl.toString()
                    imagesList.add(imageUrl)
                    imagesAdapter.notifyItemInserted(imagesList.size - 1)

                    // Đồng bộ product.images với imagesList
                    product.images.clear()
                    product.images.addAll(imagesList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }



    private fun saveChanges() {
        try {
            val updatedProduct = product.copy(
                name = binding.nameEt.text.toString(),
                description = binding.descriptionEt.text.toString(),
                price = binding.priceEt.text.toString().toDouble(),
                stock = binding.stockEt.text.toString().toInt(),
                categoryId = categories[binding.categorySpinner.selectedItemPosition].id,
                images = ArrayList(imagesList),
                colors = product.colors,
                sizes = product.sizes
            )

            val productRef = database.getReference("products").child(product.productId)
            productRef.setValue(updatedProduct)
                .addOnSuccessListener {
                    // Update the local product object
                    product = updatedProduct

                    // Notify the parent activity
                    setResult(RESULT_OK)

                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
        }
    }

}