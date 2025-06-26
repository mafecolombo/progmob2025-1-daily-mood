package com.android.dailymood.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.android.dailymood.R
import com.android.dailymood.databinding.FragmentHomeBinding
import androidx.fragment.app.commit
import com.android.dailymood.ui.fragments.home.MoodChartFragment
import com.android.dailymood.ui.fragments.home.MoodHistoryFragment
import com.android.dailymood.ui.fragments.home.ProfileFragment
import com.android.dailymood.ui.fragments.home.RegisterMoodFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Inicializa com a tela de registrar humor
        childFragmentManager.commit {
            replace(R.id.home_fragment_container, RegisterMoodFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_register -> {
                    childFragmentManager.commit {
                        replace(R.id.home_fragment_container, RegisterMoodFragment())
                    }
                    true
                }
                R.id.nav_history -> {
                    childFragmentManager.commit {
                        replace(R.id.home_fragment_container, MoodHistoryFragment())
                    }
                    true
                }
                R.id.nav_charts -> {
                    childFragmentManager.commit {
                        replace(R.id.home_fragment_container, MoodChartFragment())
                    }
                    true
                }
                R.id.nav_profile -> {
                    childFragmentManager.commit {
                        replace(R.id.home_fragment_container, ProfileFragment())
                    }
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
