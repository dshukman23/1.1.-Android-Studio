package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.formatCount
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем ViewModel
        val viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        // Наблюдаем за изменениями поста
        viewModel.post.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likeCount.text = formatCount(post.likes)
                repostCount.text = formatCount(post.shares)

                like24.setImageResource(
                    if (post.likeByMe) R.drawable.ic_liked_24
                    else R.drawable.ic_like_24
                )
            }
        }

        // Обработчики кликов
        with(binding) {
            like24.setOnClickListener {
                println("CLICK: like24")
                viewModel.like()
            }

            share.setOnClickListener {
                println("CLICK: share")
                viewModel.share()
            }

            avatar.setOnClickListener {
                println("CLICK: avatar")
            }

            root.setOnClickListener {
                println("CLICK: root")
            }
        }
    }
}