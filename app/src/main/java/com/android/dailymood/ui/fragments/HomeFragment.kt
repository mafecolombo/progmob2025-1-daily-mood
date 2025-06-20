package com.android.dailymood.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.dailymood.R
import com.android.dailymood.databinding.FragmentHomeBinding
import com.android.dailymood.utils.SessionManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnLogout.setOnClickListener {
            SessionManager.clearSession(requireContext())
            Toast.makeText(requireContext(), "Sessão encerrada!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
