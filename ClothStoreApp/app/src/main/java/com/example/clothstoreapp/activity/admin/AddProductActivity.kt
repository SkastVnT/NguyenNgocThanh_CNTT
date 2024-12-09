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
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clothstoreapp.Adapter.SelectedImagesAdapter
import com.example.clothstoreapp.Model.ItemsModel
import com.example.clothstoreapp.R
import com.example.clothstoreapp.databinding.ActivityAddProductBinding
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private val selectedImages = mutableListOf<Uri>()
    private val imageAdapter = SelectedImagesAdapter()
    private val defaultColors = listOf("Black", "White", "Red", "Blue", "Grey")
    private val defaultSizes = listOf("S", "M", "L", "XL", "XXL")
    private val selectedColors = mutableSetOf<String>()
    private val selectedSizes = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        setupToolbar()
        setupCategorySpinner()
        setupColorsChips()
        setupSizesChips()
        setupImageSelection()
        setupSaveButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupCategorySpinner() {
        val categories = mutableListOf<String>() // Danh sách danh mục
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.spinnerCategory.setAdapter(adapter)

        // Lấy danh sách danh mục từ Firebase
        database.getReference("categories").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categories.clear() // Xóa danh sách cũ
                for (categorySnapshot in snapshot.children) {
                    val categoryName = categorySnapshot.child("name").getValue(String::class.java)
                    categoryName?.let { categories.add(it) } // Thêm danh mục vào danh sách
                }
                adapter.notifyDataSetChanged() // Cập nhật giao diện
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddProductActivity, "Failed to load categories", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupColorsChips() {
        // Thêm các màu mặc định
        defaultColors.forEach { color ->
            val chip = createColorChip(color)
            binding.chipGroupColors.addView(chip)
        }

        binding.btnAddColor.setOnClickListener {
            val colorInput = EditText(this)
            colorInput.hint = "Enter a color (e.g., Red, Blue)"
            AlertDialog.Builder(this)
                .setTitle("Add Color")
                .setView(colorInput)
                .setPositiveButton("Add") { _, _ ->
                    val color = colorInput.text.toString().trim()
                    if (color.isNotEmpty() && !colorExists(color)) {
                        addColorChip(color)
                    } else if (colorExists(color)) {
                        Toast.makeText(this, "Color already exists", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun colorExists(color: String): Boolean {
        return binding.chipGroupColors.children
            .filter { it is Chip }
            .map { (it as Chip).text.toString() }
            .any { it.equals(color, ignoreCase = true) }
    }

    private fun createColorChip(color: String): Chip {
        return Chip(this).apply {
            text = color
            isCheckable = true
            isCheckedIconVisible = true
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedColors.add(color)
                } else {
                    selectedColors.remove(color)
                }
            }
        }
    }
    private fun addColorChip(color: String) {
        val chip = Chip(this).apply {
            text = color
            isCheckable = true  // Thêm thuộc tính này
            isCheckedIconVisible = true  // Thêm thuộc tính này
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                binding.chipGroupColors.removeView(this)
                selectedColors.remove(color)  // Xóa khỏi danh sách đã chọn
            }
            setOnCheckedChangeListener { _, isChecked ->  // Thêm sự kiện này
                if (isChecked) {
                    selectedColors.add(color)
                } else {
                    selectedColors.remove(color)
                }
            }
        }
        binding.chipGroupColors.addView(chip)
    }

    private fun setupSizesChips() {
        // Thêm các size mặc định
        defaultSizes.forEach { size ->
            val chip = createSizeChip(size)
            binding.chipGroupSizes.addView(chip)
        }

        binding.btnAddSize.setOnClickListener {
            val sizeInput = EditText(this)
            sizeInput.hint = "Enter a size (e.g., S, M, L)"
            AlertDialog.Builder(this)
                .setTitle("Add Size")
                .setView(sizeInput)
                .setPositiveButton("Add") { _, _ ->
                    val size = sizeInput.text.toString().trim()
                    if (size.isNotEmpty() && !sizeExists(size)) {
                        addSizeChip(size)
                    } else if (sizeExists(size)) {
                        Toast.makeText(this, "Size already exists", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun sizeExists(size: String): Boolean {
        return binding.chipGroupSizes.children
            .filter { it is Chip }
            .map { (it as Chip).text.toString() }
            .any { it.equals(size, ignoreCase = true) }
    }

    private fun createSizeChip(size: String): Chip {
        return Chip(this).apply {
            text = size
            isCheckable = true
            isCheckedIconVisible = true
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedSizes.add(size)
                } else {
                    selectedSizes.remove(size)
                }
            }
        }
    }

    private fun addSizeChip(size: String) {
        val chip = Chip(this).apply {
            text = size
            isCheckable = true  // Thêm thuộc tính này
            isCheckedIconVisible = true  // Thêm thuộc tính này
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                binding.chipGroupSizes.removeView(this)
                selectedSizes.remove(size)  // Xóa khỏi danh sách đã chọn
            }
            setOnCheckedChangeListener { _, isChecked ->  // Thêm sự kiện này
                if (isChecked) {
                    selectedSizes.add(size)
                } else {
                    selectedSizes.remove(size)
                }
            }
        }
        binding.chipGroupSizes.addView(chip)
    }

    private fun setupImageSelection() {
        binding.btnAddImages.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, PICK_IMAGES_REQUEST)
        }

        binding.rvSelectedImages.apply {
            layoutManager = LinearLayoutManager(this@AddProductActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
        }
    }

    private fun setupSaveButton() {
        binding.btnSaveProduct.setOnClickListener {
            val name = binding.etProductName.text.toString()
            val description = binding.etDescription.text.toString()
            val price = binding.etPrice.text.toString().toDoubleOrNull()
            val stock = binding.etStock.text.toString().toIntOrNull()
            val category = binding.spinnerCategory.text.toString()

            val selectedColors = binding.chipGroupColors
                .children
                .filter { it is Chip }
                .map { (it as Chip).text.toString() }
                .toList()

            val selectedSizes = binding.chipGroupSizes
                .children
                .filter { it is Chip }
                .map { (it as Chip).text.toString() }
                .toList()

            if (validateInput(name, description, price, stock, category, selectedColors, selectedSizes)) {
                uploadImages { imageUrls ->
                    saveProduct(
                        name, description, price!!, stock!!, category,
                        selectedColors, selectedSizes, imageUrls
                    )
                }
            }
        }
    }


    private fun uploadImages(onComplete: (List<String>) -> Unit) {
        val imageUrls = mutableListOf<String>()
        var uploadedCount = 0

        selectedImages.forEach { uri ->
            val imageRef = storage.reference.child("products/${System.currentTimeMillis()}_${uri.lastPathSegment}")
            imageRef.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    imageRef.downloadUrl
                }
                .addOnSuccessListener { downloadUri ->
                    imageUrls.add(downloadUri.toString())
                    uploadedCount++
                    if (uploadedCount == selectedImages.size) {
                        onComplete(imageUrls)
                    }
                }
        }
    }

    private fun saveProduct(
        name: String,
        description: String,
        price: Double,
        stock: Int,
        category: String,
        colors: List<String>,
        sizes: List<String>,
        imageUrls: List<String>
    ) {
        val productsRef = database.reference.child("products")
        val newProductKey = productsRef.push().key ?: return

        val product = ItemsModel(
            productId = newProductKey,
            name = name,
            description = description,
            price = price,
            categoryId = category,
            colors = ArrayList(selectedColors),  // Sử dụng selectedColors thay vì colors
            images = ArrayList(imageUrls),
            sizes = ArrayList(selectedSizes),  // Sử dụng selectedSizes thay vì sizes
            stock = stock
        )

        productsRef.child(newProductKey).setValue(product)
            .addOnSuccessListener {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateInput(
        name: String,
        description: String,
        price: Double?,
        stock: Int?,
        category: String,
        colors: List<String>,
        sizes: List<String>
    ): Boolean {
        if (name.isEmpty() || description.isEmpty() || price == null ||
            stock == null || category.isEmpty() || colors.isEmpty() || sizes.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK) {
            data?.let {
                if (it.clipData != null) {
                    val count = it.clipData!!.itemCount
                    for (i in 0 until count) {
                        selectedImages.add(it.clipData!!.getItemAt(i).uri)
                    }
                } else {
                    it.data?.let { uri -> selectedImages.add(uri) }
                }
                imageAdapter.submitList(selectedImages.toList())
            }
        }
    }

    companion object {
        private const val PICK_IMAGES_REQUEST = 1
    }
}