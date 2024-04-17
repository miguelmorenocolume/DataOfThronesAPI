package com.example.dataofthronesapi.ui.characterdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dataofthronesapi.R
import com.example.dataofthronesapi.databinding.FragmentCharacterDetailsBinding
import kotlinx.coroutines.launch

class CharacterDetailsFragment : Fragment() {

    companion object {
        const val DRAWABLE = "drawable"
    }

    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding
        get() = _binding!!

    val args: CharacterDetailsFragmentArgs by navArgs()

    private val characterDetailsVM by viewModels<CharacterDetailsVM> { CharacterDetailsVM.Factory }

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
        _binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        characterDetailsVM.setCharacter(args.idCharacter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                characterDetailsVM.uiState.collect { characterState ->
                    if (!characterState.isLoading) {
                        binding.pbLoadingDetails!!.isVisible = false
                        characterState.character?.let {
                            val context = binding.imageDetail.context
                            binding.tvFullName.text = characterState.character.fullName
                            binding.tvFirstName.text = characterState.character.firstName
                            binding.tvLastName.text = characterState.character.lastName
                            binding.tvHouseDetail.text = characterState.character.family
                            binding.tvTitle.text = characterState.character.title
                            binding.tvUrl.text = characterState.character.imageUrl
                            Glide.with(context)
                                .load(characterState.character.imageUrl)
                                .centerCrop()
                                .into(binding.imageDetail)
                        }
                    } else {
                        binding.pbLoadingDetails!!.isVisible = true
                        binding.tvFullName.text = getString(R.string.loading_character)
                        binding.tvFirstName.text = ""
                        binding.tvLastName.text = ""
                        binding.tvHouseDetail.text = ""
                        binding.tvTitle.text = ""
                        binding.tvUrl.text = ""

                    }
                }
            }
        }
    }
}