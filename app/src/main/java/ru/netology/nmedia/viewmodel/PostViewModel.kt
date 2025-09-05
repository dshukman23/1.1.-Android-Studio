package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl


class PostViewModel : ViewModel() {
    companion object {
        val empty = Post(
            id = 0,
            author = "",
            published = "",
            content = "",
            likes = 0,
            shares = 0,
            likeByMe = false,
            views = 0
        )
    }

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun save(text: String) {
        edited.value?.let {
            val content = text.trim()
            if (content != it.content) {
                repository.save(it.copy(content = content))
            }
        }
        edited.value = empty
    }
    fun edit(post: Post) {
        edited.value = post
    }
    fun cancelEdit() {
        edited.value = empty
    }
    fun viewsById(id: Long) = repository.views(id)
    fun markAsViewed(id: Long) = repository.markAsViewed(id)
    fun onPostShown(post: Post) {
        if (!post.viewed) {
            repository.markAsViewed(post.id)
        }
    }
}