package com.example.dataofthronesapi.ui.favoritecharacterdetails

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dataofthronesapi.R
import com.example.dataofthronesapi.adapter.CommentAdapter
import com.example.dataofthronesapi.data.Comment
import com.example.dataofthronesapi.databinding.FragmentFavCharacterDetailsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class FavCharacterDetailsFragment : Fragment() {

    companion object {
        const val DRAWABLE = "drawable"
    }

    private var _binding: FragmentFavCharacterDetailsBinding? = null
    private val binding
        get() = _binding!!

    val args: FavCharacterDetailsFragmentArgs by navArgs()

    private val favCharacterDetailsVM by viewModels<FavCharacterDetailsVM> { FavCharacterDetailsVM.Factory }

    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavCharacterDetailsBinding.inflate(inflater, container, false)

        favCharacterDetailsVM.setCharacter(args.idCharacter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
        setListeners()


    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()

        }
        binding.floatingActionButton2.setOnClickListener() {
            showAddCommentDialog()
        }
    }

    private fun initRecyclerView(comments: List<Comment>) {
        commentAdapter = CommentAdapter(comments)
        binding.rvComentarios.adapter = commentAdapter
        binding.rvComentarios.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )

    }

    private fun showAddCommentDialog() {
        val editText = EditText(requireContext())
        editText.inputType = InputType.TYPE_CLASS_TEXT

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_comment)
            .setMessage(R.string.write_comment)
            .setView(editText)
            .setPositiveButton(R.string.add) { _, _ ->
                val commentText = editText.text.toString()
                if (commentText.isNotBlank()) {
                    favCharacterDetailsVM.setComment(commentText, args.idCharacter)
                } else {
                    Snackbar.make(
                        requireView(),
                        R.string.comment_is_empty,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.show()
    }


    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favCharacterDetailsVM.uiState.collect { characterState ->
                    initRecyclerView(characterState.comments)
                    binding.pbLoadingDetails!!.isVisible = false
                    characterState.character?.let {
                        val context = binding.imageDetail.context
                        binding.tvFullName.text = characterState.character.fullName
                        binding.tvTitle.text = characterState.character.title
                        Glide.with(context)
                            .load(characterState.character.imageUrl)
                            .centerCrop()
                            .into(binding.imageDetail)

                    }

                }
            }
        }
    }
}