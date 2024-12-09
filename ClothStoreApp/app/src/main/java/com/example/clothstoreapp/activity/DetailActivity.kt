package com.example.clothstoreapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clothstoreapp.Adapter.ColorAdapter
import com.example.clothstoreapp.Adapter.SizeAdapter
import com.example.clothstoreapp.Adapter.SliderAdapter
import com.example.clothstoreapp.Model.ItemsModel
import com.example.clothstoreapp.Model.SliderModel
import com.example.clothstoreapp.R
import com.example.clothstoreapp.databinding.ActivityDetailBinding
import com.example.project1762.Helper.ManagmentCart

class DetailActivity : BaseActivity() {
    private lateinit var binding:ActivityDetailBinding
    private lateinit var item:ItemsModel
    private var numberOder=1
    private lateinit var managmentCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managmentCart=ManagmentCart(this)

        getBundle()
        banners()
        initList()

        }

    private fun initList() {
        var sizeList=ArrayList<String>()
        for (size in item.sizes) {
            sizeList.add(size.toString())
        }
            binding.sizeList.adapter=SizeAdapter(sizeList)
            binding.sizeList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val colorList=ArrayList<String>()
        for (imageUrl in item.images){
            colorList.add(imageUrl)
        }
        binding.colorList.adapter=ColorAdapter(colorList)
        binding.colorList.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    private fun banners() {
        val sliderItems=ArrayList<SliderModel>()
        for (imageUrl in item.images){
            sliderItems.add(SliderModel(imageUrl))

        }
        binding.slider.adapter=SliderAdapter(sliderItems,binding.slider)
        binding.slider.clipToPadding=true
        binding.slider.clipChildren=true
        binding.slider.offscreenPageLimit=1


        if(sliderItems.size>1) {
            binding.dotindicator.visibility= View.VISIBLE
            binding.dotindicator.attachTo(binding.slider)
        }
    }

    private fun getBundle(){
        item=intent.getParcelableExtra("object")!!

        binding.titleTxt.text=item.name
        binding.descriptionTxt.text=item.description
        binding.priceTxt.text="$"+item.price
        binding.addToCartBtn.setOnClickListener{
            item.numberInCart=numberOder
            managmentCart.insertFood(item)
        }
        binding.backBtn.setOnClickListener{finish()}
        binding.cartBtn.setOnClickListener{
            startActivity(Intent(this@DetailActivity, CartActivity::class.java ))

        }
    }
    }
