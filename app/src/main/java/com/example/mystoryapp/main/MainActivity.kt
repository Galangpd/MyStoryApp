package com.example.mystoryapp.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.adapter.StoryAdapter
import com.example.mystoryapp.ViewModelFactory
import com.example.mystoryapp.WelcomeActivity
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.maps.MapsActivity
import com.example.mystoryapp.story.UploadStoryActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(applicationContext, true)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        showLoading(true)
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            else{
                showLoading(false)
                val adapter = StoryAdapter()
                binding.rvStory.adapter = adapter

                viewModel.getStories(user.token).observe(this) {
                    adapter.submitData(lifecycle, it)
                }
            }
        }



        clickButton()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.logout -> {
                    viewModel.logout()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    true
                }
            }
            return super.onOptionsItemSelected(item)
    }

    fun clickButton(){
        binding.fab.setOnClickListener {
            val intent = Intent(this, UploadStoryActivity::class.java)
            startActivity(intent)
            true
        }
        binding.btnMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            true
        }
    }

    fun showLoading(isLoading: Boolean){
        binding.progress.visibility =if(isLoading) View.VISIBLE else View.GONE
    }
}
