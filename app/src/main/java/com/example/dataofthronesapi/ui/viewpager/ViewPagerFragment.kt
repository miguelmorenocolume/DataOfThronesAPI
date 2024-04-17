package com.example.dataofthronesapi.ui.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dataofthronesapi.R
import com.example.dataofthronesapi.databinding.FragmentViewpagerBinding
import com.example.dataofthronesapi.ui.start.StartFragment
import com.example.dataofthronesapi.ui.welcome.WelcomeFragment
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewpagerBinding? = null //
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

        _binding = FragmentViewpagerBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpNotice.adapter = NoticeAdapter(this)

        TabLayoutMediator(binding.tabNotice, binding.vpNotice) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.welcome2)
                else -> getString(R.string.start)
            }
        }.attach()
    }
}


class NoticeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        val fragment = if (position == 0)
            WelcomeFragment()
        else
            StartFragment()

        return fragment
    }
}