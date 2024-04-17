package com.example.dataofthronesapi.ui.characterlist

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dataofthronesapi.R
import com.example.dataofthronesapi.adapter.CharacterAdapter
import com.example.dataofthronesapi.data.Character
import com.example.dataofthronesapi.databinding.FragmentCharacterListBinding
import com.google.android.material.snackbar.Snackbar


import kotlinx.coroutines.launch

class CharacterListFragment : Fragment() {

    private var _binding: FragmentCharacterListBinding? = null
    val binding
        get() = _binding!!

    private val characterListVM: CharacterListVM by viewModels<CharacterListVM> { CharacterListVM.Factory }

    private lateinit var characterAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        setListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeCharacterList()
        setCollectors()
    }

    private fun setListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_List -> {
                    true
                }

                R.id.bottom_Fav -> {
                    findNavController().navigate(R.id.action_characterListFragment_to_favCharacterFragment)
                    true
                }

                R.id.bottom_User -> {
                    findNavController().navigate(R.id.action_characterListFragment_to_userInfoFragment)
                    true
                }

                else -> false
            }
        }

    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                characterListVM.uiState.collect { characterState ->
                    if (!characterState.isLoading) {
                        binding.pbLoading.isVisible = false
                        characterAdapter.setCharacterList(characterState.characterList)
                        characterAdapter.notifyDataSetChanged()
                        binding.textView.text = getString(R.string.characters)
                    } else {
                        binding.pbLoading.isVisible = true
                        binding.textView.text = getString(R.string.loading)
                    }
                    characterState.message?.let {
                        Snackbar.make(requireView(), characterState.message, Snackbar.LENGTH_SHORT)
                            .show()
                        characterListVM.messageShown()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        characterAdapter = CharacterAdapter(
            mutableListOf(),
            onClickCharacter = { characterId -> selectCharacter(characterId) },
            onClickFavorite = { character -> addFavorite(character) }
        )
        binding.rvCharacters.adapter = characterAdapter
        val layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            } else {
                binding.textView.visibility = View.GONE
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        binding.rvCharacters.layoutManager = layoutManager
    }

    private fun observeCharacterList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                characterListVM.uiState.collect { characterListUiState ->
                    if (!characterListUiState.isLoading) {
                        binding.pbLoading.isVisible = false
                        characterAdapter.setCharacterList(characterListUiState.characterList)
                    } else {
                        binding.pbLoading.isVisible = true
                    }
                }
            }
        }
    }

    private fun addFavorite(character: Character) {
        characterListVM.insertCharacter(character)
    }

    private fun selectCharacter(characterId: Int) {
        val action =
            CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailsFragment(
                characterId
            )
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}