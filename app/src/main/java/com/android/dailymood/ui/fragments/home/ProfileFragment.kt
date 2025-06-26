package com.android.dailymood.ui.fragments.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.dailymood.data.local.dao.AppDatabase
import com.android.dailymood.databinding.FragmentProfileBinding
import com.android.dailymood.utils.SessionManager
import kotlinx.coroutines.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val context = requireContext()
        val db = AppDatabase.getDatabase(context)
        val userId = SessionManager.getLoggedInUser(context)

        scope.launch {
            val user = userId?.let { db.userDao().getUserById(it) }

            withContext(Dispatchers.Main) {
                user?.let {
                    binding.tvName.text = it.name
                    binding.tvEmail.text = it.email
                    if (it.photoUri != null) {
                        val bitmap = BitmapFactory.decodeFile(it.photoUri)
                        binding.ivProfilePic.setImageBitmap(bitmap)
                    }
                }
            }
        }

        binding.btnLogout.setOnClickListener {
            SessionManager.clearSession(requireContext())
            findNavController().navigate(com.android.dailymood.R.id.action_homeFragment_to_loginFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scope.cancel()
    }
}
