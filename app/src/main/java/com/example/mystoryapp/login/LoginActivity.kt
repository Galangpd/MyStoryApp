package com.example.mystoryapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.mystoryapp.UserModel
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.Result
import com.example.mystoryapp.ViewModelFactory
import com.example.mystoryapp.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAction()
    }

    private fun setUpAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.loginUser(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            val token = result.data.loginResult?.token
                            if (token != null) {
                                lifecycleScope.launch {
                                    viewModel.saveSession(UserModel(email, token))
                                }
                            }
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeayy!!")
                                setMessage("Anda berhasil Login")
                                setPositiveButton("Masuk") { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}