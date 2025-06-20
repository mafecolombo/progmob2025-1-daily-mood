package com.android.dailymood.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.dailymood.R
import com.android.dailymood.data.local.dao.AppDatabase
import com.android.dailymood.databinding.FragmentLoginBinding
import com.android.dailymood.utils.HashUtils
import com.android.dailymood.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getDatabase(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val user = db.userDao().getUserByEmail(email)

                if (user == null) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Usuário não encontrado!", Toast.LENGTH_SHORT).show()
                    }
                } else if (user.passwordHash != HashUtils.sha256(password)) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Senha incorreta!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    SessionManager.setLoggedInUser(requireContext(), user.id)

                    requireActivity().runOnUiThread {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
            }
        }

        binding.btnGoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
