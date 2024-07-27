package com.example.mystoryapp.story

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.mystoryapp.R
import com.example.mystoryapp.ViewModelFactory
import com.example.mystoryapp.WelcomeActivity
import com.example.mystoryapp.databinding.ActivityUploadStoryBinding
import com.example.mystoryapp.getImageUri
import com.example.mystoryapp.main.MainActivity
import com.example.mystoryapp.uriToFile

class UploadStoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<UploadStoryViewModel> {
        ViewModelFactory.getInstance(applicationContext, true)
    }
    private lateinit var binding: ActivityUploadStoryBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { openGallery() }
        binding.cameraButton.setOnClickListener { openCamera() }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                binding.uploadButton.setOnClickListener {
                    uploadImage(user.token)
                    viewModel.successLiveData.observe(this) { success ->
                        if (success) {
                            Toast.makeText(
                                this,
                                (R.string.story_uploaded_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToMainActivity()
                        }
                    }
                }
            }
        }
    }

    private fun openCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage(token: String) {
        val description = binding.editDescription.text.toString()
        if (description.isBlank()) {
            Toast.makeText(this, (R.string.enter_a_description), Toast.LENGTH_SHORT).show()
        }
        currentImageUri?.let { uri ->
            val file = uriToFile(uri, this)
            viewModel.uploadStory(token, description, file)
        } ?: Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()

    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}