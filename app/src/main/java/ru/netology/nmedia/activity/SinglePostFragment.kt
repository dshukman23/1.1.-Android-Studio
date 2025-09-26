package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentSinglePostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class SinglePostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSinglePostBinding.inflate(inflater, container, false)
        val postId = requireArguments().getLong("postId")

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            PostViewHolder(binding.post, object : OnInteractionListener {
                override fun like(post: Post) = viewModel.likeById(post.id)
                override fun share(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    }
                    val chooser = Intent.createChooser(intent, getString(R.string.description_post_share))
                    startActivity(chooser)
                    viewModel.shareById(post.id)
                }
                override fun remove(post: Post) {
                    viewModel.removeById(post.id)
                    findNavController().popBackStack() // Возврат в список
                }
                override fun edit(post: Post) {
                    viewModel.edit(post)
                    findNavController().navigate(R.id.action_singlePostFragment_to_newPostFragment)
                }
                override fun onPostShown(post: Post) {
                    if (!post.viewed) {
                        viewModel.onPostShown(post)
                    }
                }
                override fun playVideo(url: String) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = url.trim().toUri()
                        }
                        if (intent.resolveActivity(requireContext().packageManager) != null) {
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Нет приложения для просмотра видео",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (_: Exception) {
                        Toast.makeText(
                            requireContext(),
                            "Не удалось открыть видео",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onPostClicked(post: Post) {
                    findNavController().navigate(
                        R.id.action_feedFragment_to_singlePostFragment, bundleOf("postId" to post.id)
                    )                }
            }).bind(post)
        }

        return binding.root
    }
}