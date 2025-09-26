package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl


class PostViewModel(application: Application) : AndroidViewModel(application) {
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

    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun edit(post: Post) {
        edited.value = post
    }

    fun onPostShown(post: Post) {
        if (!post.viewed) {
            repository.markAsViewed(post.id)
        }
    }

    fun cancelEdit() {
        edited.value = empty
    }

    fun viewsById(id: Long) = repository.views(id)
    fun markAsViewed(id: Long) = repository.markAsViewed(id)

    fun save(post: Post) {
        val text = post.content.trim()
        if (text.isNotEmpty()) {
            repository.save(post)
        }
        edited.value = empty
    }
}