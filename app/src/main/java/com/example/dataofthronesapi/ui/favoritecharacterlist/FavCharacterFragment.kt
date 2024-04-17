package com.example.dataofthronesapi.ui.favoritecharacterlist

import android.annotation.SuppressLint
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
import com.example.dataofthronesapi.adapter.FavCharacterAdapter
import com.example.dataofthronesapi.data.Character
import com.example.dataofthronesapi.databinding.FragmentFavoriteCharacterListBinding
import com.example.dataofthronesapi.ui.characterlist.CharacterListFragmentDirections
import com.example.dataofthronesapi.ui.characterlist.CharacterListVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class FavCharacterFragment : Fragment() {

    private var _binding: FragmentFavoriteCharacterListBinding? = null
    val binding
        get() = _binding!!

    private val favCharacterListVM: FavCharacterVM by viewModels<FavCharacterVM> { FavCharacterVM.Factory }

    private lateinit var favCharacterAdapter: FavCharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteCharacterListBinding.inflate(inflater, container, false)
        setListeners()
        binding.textView.text = "FAVORITOS"
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
                    findNavController().navigate(R.id.action_favCharacterFragment_to_characterListFragment)
                    true
                }

                R.id.bottom_Fav -> {
                    true
                }

                R.id.bottom_User -> {
                    findNavController().navigate(R.id.action_favCharacterFragment_to_userInfoFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favCharacterListVM.uiState.collect { characterState ->
                    binding.pbLoading.isVisible = false
                    favCharacterAdapter.setCharacterList(characterState.characterList)
                    favCharacterAdapter.notifyDataSetChanged()
                    binding.textView.text = getString(R.string.fav_character)

                    characterState.message?.let {
                        Snackbar.make(requireView(), characterState.message, Snackbar.LENGTH_SHORT)
                            .show()
                        favCharacterListVM.messageShown()
                    }

                }
            }
        }
    }

    private fun initRecyclerView() {
        favCharacterAdapter = FavCharacterAdapter(
            mutableListOf(),
            onClickCharacter = { characterId -> selectCharacter(characterId) },
            onClickDelete = { character -> confirmDeleteCharacter(character) }
        )
        binding.rvCharacters.adapter = favCharacterAdapter
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
                favCharacterListVM.uiState.collect { favCharacterUiState ->

                    binding.pbLoading.isVisible = false
                    favCharacterAdapter.setCharacterList(favCharacterUiState.characterList)

                }
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun confirmDeleteCharacter(character: Character) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete))
            .setMessage(
                resources.getString(
                    R.string.confirm_delete_message,
                    character.firstName

                )
            )
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                deleteFavorite(character)
            }
            .show()
    }

    private fun deleteFavorite(character: Character) {
        favCharacterListVM.deleteCharacter(character)
    }

    private fun selectCharacter(characterId: Int) {
        val action =
            FavCharacterFragmentDirections.actionFavCharacterFragmentToFavCharacterDetailsFragment(
                characterId
            )
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}