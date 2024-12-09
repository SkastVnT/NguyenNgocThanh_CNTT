package com.example.clothstoreapp.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clothstoreapp.Model.BrandModel
import com.example.clothstoreapp.Model.ItemsModel
import com.example.clothstoreapp.Model.OrderModel
import com.example.clothstoreapp.Model.SliderModel
import com.example.clothstoreapp.Model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class MainViewModel :ViewModel(){


    private val _orders = MutableLiveData<MutableList<OrderModel>>()

    private val _currentUser = MutableLiveData<UserModel>()

    private val firebaseDatabase=FirebaseDatabase.getInstance()

    private val _brand=MutableLiveData<MutableList<BrandModel>>()

    private val _popular =MutableLiveData<MutableList<ItemsModel>>()
    private val listeners = mutableListOf<ValueEventListener>()

    private val _banner=MutableLiveData<List<SliderModel>>()

    val banners:LiveData<List<SliderModel>> = _banner
    val brands:LiveData<MutableList<BrandModel>> = _brand
    val popular:LiveData<MutableList<ItemsModel>> = _popular
    val orders: LiveData<MutableList<OrderModel>> = _orders
    val currentUser: LiveData<UserModel> = _currentUser
    fun loadBanners(){
        val Ref=firebaseDatabase.getReference("banner")
        Ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists= mutableListOf<SliderModel>()
                for (childSnapshot in snapshot.children ){
                    val list=childSnapshot.getValue(SliderModel::class.java)
                    if(list!=null) {
                        lists.add(list)
                    }
                }
                _banner.value=lists
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun loadBrand() {
        val Ref = firebaseDatabase.getReference("categories")
        Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<BrandModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(BrandModel::class.java)
                    if (list != null) {
                        // Nếu id rỗng, gán id từ key của Firebase
                        list.id = childSnapshot.key ?: ""
                        lists.add(list)
                        Log.d("BrandData", "Loaded Brand ID: ${list.id}")
                    }
                }
                _brand.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })
    }


    fun loadPopular() {
        val Ref = firebaseDatabase.getReference("products")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lists = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val list = childSnapshot.getValue(ItemsModel::class.java)
                    if (list != null) {
                        lists.add(list)
                    }
                }
                _popular.value = lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainViewModel", "Error loading popular items: ${error.message}")
            }
        }
        Ref.addValueEventListener(listener)
        listeners.add(listener)
    }


    fun loadOrders() {
        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
        if (currentUserEmail != null) {
            val ordersRef = firebaseDatabase.getReference("Orders")
            ordersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ordersList = mutableListOf<OrderModel>()
                    for (orderSnapshot in snapshot.children) {
                        val order = orderSnapshot.getValue(OrderModel::class.java)
                        if (order != null && order.email == currentUserEmail) {
                            ordersList.add(order)
                        }
                    }
                    ordersList.sortByDescending { it.timestamp }

                    _orders.value = ordersList

                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý lỗi
                }
            })
        }
    }

    fun loadUserProfile() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            try {
                firebaseDatabase.getReference("users").child(currentUserId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            try {
                                val user = snapshot.getValue(UserModel::class.java)
                                _currentUser.value = user
                            } catch (e: Exception) {
                                Log.e("MainViewModel", "Error parsing user data: ${e.message}")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("MainViewModel", "Error loading user profile: ${error.message}")
                        }
                    })
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error setting up database listener: ${e.message}")
            }
        }
    }
    fun updateUserProfile(updatedUser: UserModel, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            firebaseDatabase.getReference("users").child(currentUserId)
                .setValue(updatedUser)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onError(e.message ?: "Update failed")
                }
        }
    }

    fun updateUserAvatar(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onProgress: (Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            onError("User not logged in")
            return
        }

        val storageRef = FirebaseStorage.getInstance().reference
        val avatarRef = storageRef.child("avatars/${currentUser.uid}/profile.jpg")

        avatarRef.putFile(imageUri)
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                onProgress(progress)
            }
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception("Unknown error")
                }
                avatarRef.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                val userRef = firebaseDatabase.getReference("users").child(currentUser.uid)
                userRef.child("avatarUrl").setValue(downloadUri.toString())
                    .addOnSuccessListener {
                        onSuccess(downloadUri.toString())
                    }
                    .addOnFailureListener { e ->
                        onError("Failed to update avatar URL: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                onError("Upload failed: ${e.message}")
            }
    }

}


