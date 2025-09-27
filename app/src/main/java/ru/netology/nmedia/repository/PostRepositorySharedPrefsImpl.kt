package ru.netology.nmedia.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPrefsImpl(context: Context) : PostRepository {
   private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)

    private var nextId = 1L
    private var posts = listOf<Post>()
        set(value) {
            field = value
            sync()
        }
    private val data = MutableLiveData(posts)

    init {
        prefs.getString(KEY_POSTS, null)?.let {
            posts = gson.fromJson(it, type)
            nextId = posts.maxOfOrNull { it.id }?.inc() ?: 1
            data.value = posts
        }
    }

    private fun sync() {
        prefs.edit {
            putString(KEY_POSTS, gson.toJson(posts))
        }
    }
    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                val newLiked = !post.likeByMe
                post.copy(
                    likeByMe = newLiked,
                    likes = post.likes + if (newLiked) 1 else -1
                )
            } else post
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(shares = post.shares + 1)
            } else post
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++, author = "Me", published = "now")) + posts
        } else {
            posts.map { if (it.id == post.id) post else it }
        }
        data.value = posts.toList()
    }

    override fun views(id: Long) {
        val currentList = posts.toMutableList()
        val post = currentList.find { it.id == id } ?: return

        val updatedPost = post.copy(views = post.views + 1)

        val index = currentList.indexOf(post)
        currentList[index] = updatedPost

        posts = currentList
        data.value = posts.toList()
    }

    override fun markAsViewed(id: Long) {
        val currentPosts = posts.toMutableList()
        val post = currentPosts.find { it.id == id } ?: return

        if (!post.viewed) {
            val updatedPost = post.copy(
                views = post.views + 1,
                viewed = true
            )
            val index = currentPosts.indexOf(post)
            currentPosts[index] = updatedPost
            posts = currentPosts
            data.value = posts.toList()
        }
    }

    companion object {
        private const val KEY_POSTS = "posts"

        private val gson = Gson()
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }
}