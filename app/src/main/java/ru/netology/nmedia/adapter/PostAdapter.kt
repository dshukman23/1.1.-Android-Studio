package ru.netology.nmedia.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.formatCount

interface OnInteractionListener {
    fun like(post: Post)
    fun share(post: Post)
    fun remove(post: Post)
    fun edit(post: Post)
    fun onPostShown(post: Post)
    fun playVideo(url: String)
    fun onPostClicked(post: Post)
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    override fun onViewAttachedToWindow(holder: PostViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onPostShown()
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    private var currentPost: Post? = null

    fun bind(post: Post) {
        binding.videoGroup.visibility = if (post.video != null) View.VISIBLE else View.GONE
        currentPost = post
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like24.text = formatCount(post.likes)
            like24.isActivated = post.likeByMe
            share.text = formatCount(post.shares)
            views.text = formatCount(post.views)

            val iconRes = if (post.likeByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            like24.setIconResource(iconRes)

            binding.videoStub.setOnClickListener {
                currentPost?.video?.let { url ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(url)
                        }
                        it.context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(it.context, "Нет приложения для открытия ссылки", Toast.LENGTH_LONG).show()
                    }
                }
            }

            binding.videoPlayButton.setOnClickListener {
                currentPost?.video?.let { url ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(url)
                        }
                        it.context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(it.context, "Невозможно открыть видео", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.root.setOnClickListener {
            currentPost?.let { post ->
                onInteractionListener.onPostClicked(post)
            }
        }

        with(binding) {
            like24.setOnClickListener { currentPost?.let(onInteractionListener::like) }
            share.setOnClickListener { currentPost?.let(onInteractionListener::share) }
            menu.setOnClickListener {
                currentPost?.let { post ->
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.menu_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    onInteractionListener.remove(post)
                                    true
                                }
                                R.id.edit -> {
                                    onInteractionListener.edit(post)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }
    }

    fun onPostShown() {
        currentPost?.let { post ->
            if (!post.viewed) {
                onInteractionListener.onPostShown(post)
            }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}