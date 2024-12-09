package com.example.clothstoreapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.clothstoreapp.Adapter.BrandAdapter
import com.example.clothstoreapp.Adapter.PopularAdapter
import com.example.clothstoreapp.Model.SliderModel
import com.example.clothstoreapp.Adapter.SliderAdapter
import com.example.clothstoreapp.LoginActivity
import com.example.clothstoreapp.Model.BrandModel
import com.example.clothstoreapp.Model.ItemsModel
import com.example.clothstoreapp.R
import com.example.clothstoreapp.ViewModel.MainViewModel
import com.example.clothstoreapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityMainBinding

    private val viewModel=MainViewModel()
    private var allProducts = mutableListOf<ItemsModel>()
    private lateinit var popularAdapter: PopularAdapter
    private var filteredProducts = mutableListOf<ItemsModel>()
    private var previousMinPrice: Double? = null
    private var previousMaxPrice: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()
        initBrand()
        initPopular()
        setupSearch()
        initBottomMenu()
        initProfile()
        setupFilterByPrice()
        setupSortButton()
        initOrderButton()

    }

    private fun initOrderButton() {
        binding.orderBtn.setOnClickListener {
            startActivity(Intent(this, OrderActivity::class.java))
        }
    }


    private fun setupFilterByPrice() {
        binding.filter.setOnClickListener {
            showPriceFilterDialog()
        }
    }

    private fun initProfile() {
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, UserProfileActivity::class.java))
        }
    }


    private fun initBottomMenu() {
        binding.cartBtn.setOnClickListener{ startActivity(Intent(this@MainActivity,CartActivity::class.java))}    }

    private fun initBanner() {
        binding.progressBarBanner.visibility= View.VISIBLE
        viewModel.banners.observe(this,{items->
            banners(items)
            binding.progressBarBanner.visibility=View.GONE

        })
        viewModel.loadBanners()
    }
    private fun banners(images:List<SliderModel>) {
        binding.viewPagerSlider.adapter= SliderAdapter(images,binding.viewPagerSlider)
        binding.viewPagerSlider.clipToPadding=false
        binding.viewPagerSlider.clipChildren=false
        binding.viewPagerSlider.offscreenPageLimit=3
        binding.viewPagerSlider.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer=CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPagerSlider.setPageTransformer(compositePageTransformer)
        if(images.size>1) {
            binding.dotindicator.visibility=View.VISIBLE
            binding.dotindicator.attachTo(binding.viewPagerSlider)

        }
    }

    private fun initPopular() {
        popularAdapter = PopularAdapter(filteredProducts)  // Sử dụng filteredProducts để hiển thị
        binding.viewPopular.layoutManager = GridLayoutManager(this@MainActivity, 2)
        binding.viewPopular.adapter = popularAdapter   // Gán adapter vào RecyclerView

        // Lắng nghe dữ liệu từ ViewModel và lưu tất cả sản phẩm vào allProducts
        viewModel.popular.observe(this, Observer {
            allProducts.clear()
            allProducts.addAll(it)  // Lưu toàn bộ sản phẩm vào allProducts
            filteredProducts.clear()
            filteredProducts.addAll(allProducts)  // Hiển thị tất cả sản phẩm ban đầu
            popularAdapter.notifyDataSetChanged()  // Cập nhật adapter
            binding.progressBarPopular.visibility = View.GONE
        })
        viewModel.loadPopular()
    }

    private fun initBrand() {
        binding.progressBarBrands2.visibility = View.VISIBLE
        viewModel.brands.observe(this, Observer { brands ->
            binding.viewBrand.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter = BrandAdapter(brands) { selectedBrand ->
                if (selectedBrand != null) {
                    Log.d("BrandSelected", "Selected Brand ID: ${selectedBrand.id}")
                } else {
                    Log.d("BrandSelected", "No brand selected")
                }
                filterProductsByBrand(selectedBrand)
            }
            binding.progressBarBrands2.visibility = View.GONE
        })
        viewModel.loadBrand()
    }

    private fun filterProductsByBrand(selectedBrand: BrandModel?) {
        filteredProducts.clear()

        if (selectedBrand == null) {
            // Nếu không có brand nào được chọn, hiển thị lại tất cả sản phẩm
            filteredProducts.addAll(allProducts)
        } else {
            // Nếu có brand được chọn, lọc theo categoryId
            filteredProducts.addAll(allProducts.filter { it.categoryId == selectedBrand.id })
        }

        popularAdapter.notifyDataSetChanged()  // Cập nhật lại adapter
    }
    private fun setupSearch() {
        binding.editTextText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProductsByName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun filterProductsByName(query: String) {
        filteredProducts.clear()

        if (query.isEmpty()) {
            // If search query is empty, show all products
            filteredProducts.addAll(allProducts)
        } else {
            // Filter products based on the name
            filteredProducts.addAll(allProducts.filter {
                it.name.contains(query, ignoreCase = true)
            })
        }

        popularAdapter.notifyDataSetChanged()  // Update RecyclerView with filtered data
    }
    private fun showPriceFilterDialog() {
        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_price_filter, null)
        val minPriceInput = dialogView.findViewById<EditText>(R.id.minPrice)
        val maxPriceInput = dialogView.findViewById<EditText>(R.id.maxPrice)
        val applyButton = dialogView.findViewById<Button>(R.id.applyFilterButton)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelFilterButton)

        // If there are previously entered values, show them in the input fields
        minPriceInput.setText(previousMinPrice?.toString() ?: "")
        maxPriceInput.setText(previousMaxPrice?.toString() ?: "")

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Filter by Price")
            .setView(dialogView)
            .create()

        applyButton.setOnClickListener {
            // Get the min and max price entered by the user
            val minPrice = minPriceInput.text.toString().toDoubleOrNull()
            val maxPrice = maxPriceInput.text.toString().toDoubleOrNull()

            // Save the entered values to retain them when reopening the dialog
            previousMinPrice = minPrice
            previousMaxPrice = maxPrice

            // Set default values if either field is left blank
            val finalMinPrice = minPrice ?: 0.0
            val finalMaxPrice = maxPrice ?: Double.MAX_VALUE

            // Apply the filter and update the product list
            filterProductsByPrice(finalMinPrice, finalMaxPrice)
            dialog.dismiss()
        }


        cancelButton.setOnClickListener {
            // Simply dismiss the dialog without resetting previous values
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun filterProductsByPrice(minPrice: Double, maxPrice: Double) {
        filteredProducts.clear()

        filteredProducts.addAll(allProducts.filter {
            it.price in minPrice..maxPrice
        })

        popularAdapter.notifyDataSetChanged()  // Update the product list in the RecyclerView
    }

    private var sortState = -1 // -1: ban đầu, 0: tăng dần, 1: giảm dần

    private fun setupSortButton() {
        binding.btnSort.setOnClickListener {
            when (sortState) {
                -1 -> { // Từ trạng thái ban đầu -> tăng dần
                    binding.btnSort.setImageResource(R.drawable.up)
                    popularAdapter.sortByPriceAscending()
                    binding.btnSort.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGrey))
                    sortState = 0
                }
                0 -> { // Từ tăng dần -> giảm dần
                    binding.btnSort.setImageResource(R.drawable.down)
                    popularAdapter.sortByPriceDescending()
                    binding.btnSort.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGrey))
                    sortState = 1
                }
                1 -> { // Từ giảm dần -> trạng thái ban đầu
                    binding.btnSort.setImageResource(R.drawable.up)
                    resetProductList()
                    binding.btnSort.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    sortState = -1
                }
            }
        }
    }

    private fun resetProductList() {
        filteredProducts.clear()
        filteredProducts.addAll(allProducts)
        popularAdapter.notifyDataSetChanged()
    }
}




