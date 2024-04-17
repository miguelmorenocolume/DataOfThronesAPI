package com.example.dataofthronesapi.ui.start

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
import com.example.dataofthronesapi.data.UserPreferences
import com.example.dataofthronesapi.databinding.FragmentStartBinding
import com.example.dataofthronesapi.ui.login.LoginVM
import com.example.dataofthronesapi.ui.settings.SettingsFragmentVM
import com.example.dataofthronesapi.ui.viewpager.ViewPagerFragmentDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class StartFragment : Fragment() {

    private val startVM: StartVM by viewModels<StartVM> { StartVM.Factory }

    private var _binding: FragmentStartBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)

        setListeners()
        setCollectors()

        return binding.root
    }

    private fun setListeners() {
        binding.btnMenu.setOnClickListener {
            startVM.saveSaveSettingsStart(binding.checkBoxIntro2.isChecked)
            //findNavController().navigate(R.id.action_viewPagerFragment_to_characterListFragment)
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                startVM.uiState.collect {
                    if (it.savedSkipIntro) {
                        val action =
                            ViewPagerFragmentDirections.actionViewPagerFragmentToCharacterListFragment()
                        findNavController().navigate(action)
                    }
                    if (it.error) {
                        Snackbar.make(requireView(), "ERROR", Snackbar.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

}