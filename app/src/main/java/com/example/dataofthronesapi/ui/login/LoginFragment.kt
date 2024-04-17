package com.example.dataofthronesapi.ui.login

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
import com.example.dataofthronesapi.databinding.LoginFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding

    private val loginVM: LoginVM by viewModels<LoginVM> { LoginVM.Factory }

    var skipIntro: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        setListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()

    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginVM.uiState.collect { userSettings ->
                    binding.etUserName2.setText(userSettings.name)
                    skipIntro = userSettings.skipIntro
                }
            }
        }
    }

    private fun setListeners() {
        binding.btnNext.setOnClickListener {
            val name = binding.etUserName2.text.toString()

            if (name.isNotBlank()) {
                validateName(name)
            } else {
                Snackbar.make(
                    requireView(), getString(R.string.word_is_empty), Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validateName(name: String) {
        if (name.isNotBlank()) {
            loginVM.saveSettings(name)
            navigateToViewPagerFragment()
        } else {
            Snackbar.make(requireView(), getString(R.string.word_is_empty), Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun navigateToViewPagerFragment() {
        if (skipIntro) {
            findNavController().navigate(R.id.action_loginFragment_to_characterListFragment)
        } else {
            findNavController().navigate(R.id.action_loginFragment_to_viewPagerFragment)

        }

    }
}