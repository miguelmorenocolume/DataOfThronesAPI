package com.example.dataofthronesapi.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.dataofthronesapi.R
import com.example.dataofthronesapi.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val settingsVM: SettingsFragmentVM by viewModels<SettingsFragmentVM> { SettingsFragmentVM.Factory }

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
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

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
                settingsVM.uiState.collect { userSettings ->
                    binding.etUserName.setText(userSettings.name)
                    skipIntro = userSettings.skipIntro
                    binding.checkBoxIntro.isChecked = userSettings.skipIntro
                }
            }
        }
    }

    private fun setListeners() {
        binding.BackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.SaveBtn.setOnClickListener {
            skipIntro = binding.checkBoxIntro.isChecked
            validateName(binding.etUserName.text.toString())
        }
    }

    private fun validateName(name: String) {
        hideKeyboard(requireView())
        if (!name.isBlank()) {
            Snackbar.make(requireView(), getString(R.string.user_changed), Snackbar.LENGTH_SHORT)
                .show()
            settingsVM.saveSettings(name, skipIntro)
        } else {
            Snackbar.make(requireView(), getString(R.string.word_is_empty), Snackbar.LENGTH_SHORT)
        }

    }

}

private fun hideKeyboard(view: View) {
    val inputMethodManager =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

