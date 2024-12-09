package com.example.clothstoreapp.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.clothstoreapp.LoginActivity
import com.example.clothstoreapp.Model.UserModel
import com.example.clothstoreapp.R
import com.example.clothstoreapp.ViewModel.MainViewModel
import com.example.clothstoreapp.activity.admin.AdminManagementActivity
import com.example.clothstoreapp.databinding.ActivityUserProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        // Đặt onClick listener sau khi binding đã được khởi tạo
        binding.profileImage.setOnClickListener {
            openImageChooser()
        }

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        checkAdminRole()

        setupToolbar()
        setupClickListeners()
        observeUserData()

        viewModel.loadUserProfile()
    }

    private fun openImageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            binding.profileImage.setImageURI(selectedImageUri)
            uploadImage(selectedImageUri!!)
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val progressDialog = ProgressDialog(this).apply {
            setTitle("Uploading...")
            show()
        }

        viewModel.updateUserAvatar(
            imageUri,
            onSuccess = { downloadUrl ->
                progressDialog.dismiss()
                Toast.makeText(this, "Avatar updated successfully", Toast.LENGTH_SHORT).show()
            },
            onProgress = { progress ->
                progressDialog.setMessage("Uploaded ${progress.toInt()}%")
            },
            onError = { error ->
                progressDialog.dismiss()
                Toast.makeText(this, "Upload failed: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupClickListeners() {

            binding.adminSection.setOnClickListener {
                // Thêm flags để tránh activity bị restart
                val intent = Intent(this, AdminManagementActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }


        binding.informationSection.setOnClickListener {
            showInformationDialog()
        }

        binding.orderHistorySection.setOnClickListener {
            // Navigate to Order History
            startActivity(Intent(this, OrderActivity::class.java))
        }



        binding.logoutBtn.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun checkAdminRole() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            database.reference.child("users").child(currentUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(UserModel::class.java)
                        if (user?.role == "admin") {
                            binding.adminSection.visibility = View.VISIBLE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@UserProfileActivity,
                            "Error: ${error.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
    private fun observeUserData() {
        viewModel.currentUser.observe(this) { user ->
            user?.let {
                try {
                    binding.userName.text = it.fullName
                    binding.userEmail.text = it.email

                    if (!it.avatarUrl.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(it.avatarUrl)
                            .placeholder(R.drawable.cloth)
                            .error(R.drawable.cloth)
                            .circleCrop()
                            .into(binding.profileImage)
                    }
                } catch (e: Exception) {
                    Log.e("UserProfileActivity", "Error updating UI: ${e.message}")
                }
            }
        }
    }

    private fun showInformationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_user_information, null)
        val user = viewModel.currentUser.value

        // Bind current user data to EditTexts
        val etFullName = dialogView.findViewById<TextInputEditText>(R.id.etFullName)
        val etPhone = dialogView.findViewById<TextInputEditText>(R.id.etPhone)
        val etAddress = dialogView.findViewById<TextInputEditText>(R.id.etAddress)

        etFullName.setText(user?.fullName)
        etPhone.setText(user?.phone)
        etAddress.setText(user?.address)

        MaterialAlertDialogBuilder(this)
            .setTitle("Edit Information")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val updatedUser = UserModel(
                    fullName = etFullName.text.toString(),
                    email = user?.email ?: "",
                    phone = etPhone.text.toString(),
                    address = etAddress.text.toString(),
                    avatarUrl = user?.avatarUrl ?:""

                )

                viewModel.updateUserProfile(
                    updatedUser,
                    onSuccess = {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    },
                    onError = { error ->
                        Toast.makeText(this, "Update failed: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun createInformationView(): View {
        val view = layoutInflater.inflate(R.layout.dialog_user_information, null)
        val user = viewModel.currentUser.value

        // Bind user data to views
        view.findViewById<TextInputEditText>(R.id.etFullName).setText(user?.fullName)

        view.findViewById<TextInputEditText>(R.id.etPhone).setText(user?.phone)
        view.findViewById<TextInputEditText>(R.id.etAddress).setText(user?.address)

        return view
    }
    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                // Ghi log trạng thái
                Log.d("Logout", "User logged out successfully")
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish() // Đảm bảo Activity hiện tại bị hủy
            }
            .setNegativeButton("No", null)
            .show()
    }

}