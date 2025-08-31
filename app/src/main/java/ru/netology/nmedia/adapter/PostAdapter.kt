package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.formatCount

interface OnInteractionListener {
    fun like (post: Post)
    fun share (post: Post)
    fun remove (post: Post)
    fun edit (post: Post)
}
class PostAdapter(
    private  val onInteractionListener: OnInteractionListener
    ) :
    ListAdapter <Post, PostViewHolder>(PostDiffCallback)  {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener )
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
    private  val onInteractionListener: OnInteractionListener
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
                    onInteractionListener.like(post)
                }
                share.setOnClickListener {
                    println("CLICK: share")
                    onInteractionListener.share(post)
                }
                avatar.setOnClickListener {
                    println("CLICK: avatar")
                }
                root.setOnClickListener {
                    println("CLICK: root")
                }
                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply{
                        inflate(R.menu.menu_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId){
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