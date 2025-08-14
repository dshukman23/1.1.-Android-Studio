package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    private val _post = MutableLiveData(repository.getPost())
    val post: LiveData<Post> = _post

    fun like() {
        repository.like()
        _post.value = repository.getPost()
    }

    fun share() {
        repository.share()
        _post.value = repository.getPost()
    }
}