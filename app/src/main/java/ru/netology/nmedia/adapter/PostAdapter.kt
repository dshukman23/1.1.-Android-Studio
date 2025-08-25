package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.formatCount

typealias onItemLikeListener = (post: Post) -> Unit
typealias onItemShareListener = (post: Post) -> Unit

class PostAdapter(
    private  val onItemLikeListener: onItemLikeListener,
    private val onItemShareListener: onItemShareListener,
    ) :
    ListAdapter <Post, PostViewHolder>(PostDiffCallback)  {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onItemLikeListener, onItemShareListener )
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder (
    private val binding: CardPostBinding,
    private  val onItemLikeListener: onItemLikeListener,
    private val onItemShareListener: onItemShareListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post){
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = formatCount(post.likes)
            repostCount.text = formatCount(post.shares)
            like24.setImageResource(
                if (post.likeByMe) R.drawable.ic_liked_24
                else R.drawable.ic_like_24
            )
            // Обработчики кликов
            with(binding) {
                like24.setOnClickListener {
                    println("CLICK: like24")
                    onItemLikeListener(post)
                }
                share.setOnClickListener {
                    println("CLICK: share")
                    onItemShareListener(post)
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
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean {
        return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean {
        return  oldItem == newItem
    }

}