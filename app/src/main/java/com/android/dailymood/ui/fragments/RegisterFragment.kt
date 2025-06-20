package com.android.dailymood.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.dailymood.R
import com.android.dailymood.data.local.dao.AppDatabase
import com.android.dailymood.databinding.FragmentRegisterBinding
import com.android.dailymood.data.local.model.User
import com.android.dailymood.utils.HashUtils
import com.android.dailymood.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getDatabase(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val existingUser = db.userDao().getUserByEmail(email)

                if (existingUser != null) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Email já cadastrado!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val user = User(
                        name = name,
                        email = email,
                        passwordHash = HashUtils.sha256(password),
                        photoUri = null
                    )
                    val userId = db.userDao().insert(user).toInt()
                    SessionManager.setLoggedInUser(requireContext(), userId)

                    requireActivity().runOnUiThread {
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
