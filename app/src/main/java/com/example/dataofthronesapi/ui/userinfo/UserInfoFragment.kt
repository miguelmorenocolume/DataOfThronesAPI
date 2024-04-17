package com.example.dataofthronesapi.ui.userinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.dataofthronesapi.R
import com.example.dataofthronesapi.databinding.FragmentUserInfoBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding

    private val userInfoVM: UserInfoVM by viewModels { UserInfoVM.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)

        setListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }

    private fun setListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_List -> {
                    findNavController().navigate(R.id.action_userInfoFragment_to_characterListFragment)
                    true
                }

                R.id.bottom_Fav -> {
                    findNavController().navigate(R.id.action_userInfoFragment_to_favCharacterFragment)
                    true
                }

                R.id.bottom_User -> {

                    true
                }

                else -> false
            }
        }
        binding.floatingActionButton.setOnClickListener() {
            findNavController().navigate(R.id.action_userInfoFragment_to_settingsFragment)
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userInfoVM.uiState.collect { userInfoState ->
                    binding.tvInfo.text = userInfoState.userName
                }
            }
        }
    }
}

