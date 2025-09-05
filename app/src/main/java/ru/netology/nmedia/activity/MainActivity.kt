package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем ViewModel
        val viewModel = ViewModelProvider(this)[PostViewModel::class.java]

        val adapter = PostAdapter( object : OnInteractionListener{
            override fun like(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun share(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun remove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun edit(post: Post) {
                viewModel.edit(post)
            }

            override fun onPostShown(post: Post) {
                viewModel.onPostShown(post)
            }
        })
        binding.list.adapter = adapter

        // Наблюдаем за изменениями поста
        viewModel.data.observe(this) { posts ->
            val new = posts.size > adapter.currentList.size && adapter.currentList.isNotEmpty()
            adapter.submitList(posts){
                if (new){
                binding.list.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.edited.observe(this){
            if (it.id != 0L) {
                binding.content.setText(it.content)
                AndroidUtils.showKeyboard(binding.content)
            }
        }
        binding.save.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isBlank()){
                Toast.makeText(this@MainActivity, R.string.error_empty_content, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.save(text)

            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(binding.content)
        }

        viewModel.edited.observe(this) { post ->
            val isEditing = post.id != 0L
            binding.cancel.visibility = if (isEditing) View.VISIBLE else View.GONE
        }

        binding.cancel.setOnClickListener {
            viewModel.cancelEdit()
            binding.content.setText("")
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(binding.content)
        }

    }
}