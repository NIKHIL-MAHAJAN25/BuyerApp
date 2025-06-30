package com.nikhil.buyerapp.comprofile

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.nikhil.buyerapp.R
import com.nikhil.buyerapp.databinding.ActivityProfileScreen1Binding
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.uploadAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileScreen1 : AppCompatActivity() {
    lateinit var binding: ActivityProfileScreen1Binding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db= Firebase.firestore
    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 100
    private val MANAGE_EXTERNAL_STORAGE_REQUEST_CODE = 101
    private lateinit var supabaseClient: SupabaseClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityProfileScreen1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supabaseClient=(this.application as supabasefile).supabaseClient
        binding.profileImage2.setOnClickListener {
            checkPermission()
        }

    }
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                pickImage()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                   this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                pickImage()
            }
        }
    }

    private fun requestManage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 (API level 30) and above
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                startActivityForResult(intent, PERMISSION_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                // Handle the case where the intent is not available (maybe the device is on a lower version)
                Log.e("PermissionRequest", "Activity not found for the permission intent.")
            }
        } else {
            // Handle this case for older Android versions (below Android 11)
            Log.e("PermissionRequest", "The permission is only available on Android 11 (API level 30) and above.")
        }
//        // Request the user to open settings to allow full access
//        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//        startActivityForResult(intent, MANAGE_EXTERNAL_STORAGE_REQUEST_CODE)
    }
    private fun pickImage(){
        val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }

    private fun uploadImageToSupabase(uri: Uri) {
        val byteArray = uriToByteArray(this, uri)
        val fileName = "uploads/${System.currentTimeMillis()}.jpg"

        val bucket = supabaseClient.storage.from("sample") // Choose your bucket name

        // Use lifecycleScope for safe coroutine usage
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Upload image and handle the response
                bucket.uploadAsFlow(fileName, byteArray).collect { status ->
                    withContext(Dispatchers.Main) {
                        when (status) {
                            is UploadStatus.Progress -> {
//                                val progress = (status.totalBytesSent.toFloat() / status.contentLength * 100)
                                Log.d("Upload", "Progress%")
                            }
                            is UploadStatus.Success -> {
                                Log.d("Upload ", "Upload Success")
                                handleUploadSuccess(bucket, fileName)







                            }

                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("Upload", "Error uploading image: ${e.message}")

                }
            }
        }
    }
    private fun uriToByteArray(context: Context, uri: Uri): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
        return inputStream?.readBytes() ?: ByteArray(0)
    }
    @RequiresApi(Build.VERSION_CODES.R)


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with file operations
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                    pickImage()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            MANAGE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (Environment.isExternalStorageManager()) {
                    // The user granted permission for full access
                    Toast.makeText(this, "Full storage access granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Storage permission not granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            data?.data?.let { uri ->
                // Handle the selected image (upload it to Supabase)
                uploadImageToSupabase(uri)
            }
        }
    }
    private fun handleUploadSuccess(bucket: Any, fileName: String) {
        try {
            val imageUrl = supabaseClient.storage.from("sample").publicUrl(fileName)
            Log.d("ProfileFragment", "Generated public URL: $imageUrl")

            val currentUser = auth.currentUser
            if (currentUser != null) {
                Log.d("ProfileFragment", "Updating Firestore with new image URL")
                db.collection("Users")
                    .document(currentUser.uid)
                    .update("profileimageurl", imageUrl)
                    .addOnSuccessListener {
                        Log.d("ProfileFragment", "Firestore update successful")
                        Toast.makeText(this, "Profile image updated!", Toast.LENGTH_SHORT).show()
                        Glide.with(this)
                            .load(imageUrl)
                            .error(R.drawable.ic_launcher_background)
                            .into(binding.profileImage)
                        }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreUpdate", "Failed to update profile image URL: ${e.message}", e)
                        Toast.makeText(this, "Failed to update profile image: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Log.e("ProfileFragment", "Current user is null")
                Toast.makeText(this, "User authentication error", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Error in handleUploadSuccess: ${e.message}", e)
            Toast.makeText(this, "Error processing upload success: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}