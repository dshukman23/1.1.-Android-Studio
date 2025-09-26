package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.activity.AppActivity.Companion.textArg
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by activityViewModels()

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            binding.content.setText(post.content)
            binding.content.setSelection(post.content.length)
        }

        if (viewModel.edited.value?.content.isNullOrBlank()) {
            arguments?.textArg?.let { binding.content.setText(it) }
            val initialContent = requireActivity().intent.getStringExtra(Intent.EXTRA_TEXT)
            if (!initialContent.isNullOrBlank()) {
                binding.content.setText(initialContent)
                binding.content.setSelection(initialContent.length)
            }
        }

        binding.add.setOnClickListener {
            val content = binding.content.text.toString().trim()
            if (content.isNotEmpty()) {
                val current = viewModel.edited.value ?: PostViewModel.empty
                viewModel.save(current.copy(content = content))
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
        }
        return binding.root
    }
}
