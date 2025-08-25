package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем ViewModel
        val viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        val adapter = PostAdapter(
            onItemLikeListener = { post ->
                viewModel.likeById(post.id)
            },
            onItemShareListener = { post ->
                viewModel.shareById(post.id)
            }
        )
        binding.list.adapter = adapter

        // Наблюдаем за изменениями поста
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}