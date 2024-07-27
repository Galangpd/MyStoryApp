package com.example.mystoryapp.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mystoryapp.Result
import com.example.mystoryapp.ViewModelFactory
import com.example.mystoryapp.WelcomeActivity
import com.example.mystoryapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAction()
    }

    private fun setUpAction(){
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Validasi input
            if (name.isNotEmpty() && email.isNotEmpty() && password.length >= 8) {
                viewModel.registerUser(name, email, password).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                result.data.message.let {
                                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                                }
                                val intent = Intent(this, WelcomeActivity::class.java)
                                startActivity(intent)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            } else {
                // Menampilkan pesan error jika input tidak valid
                Toast.makeText(
                    this,
                    "Harap isi semua kolom",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.registrationResult.observe(this) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}