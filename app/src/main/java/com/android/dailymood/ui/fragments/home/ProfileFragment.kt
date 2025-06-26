package com.android.dailymood.ui.fragments.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.dailymood.data.local.dao.AppDatabase
import com.android.dailymood.databinding.FragmentProfileBinding
import com.android.dailymood.utils.SessionManager
import kotlinx.coroutines.*
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var photoUri: Uri? = null

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1001
    }

    private val pickImageLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let { updatePhoto(it) }
            }
        }

    private val takePhotoLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                photoUri?.let { updatePhoto(it) }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                    it.photoUri?.let { path ->
                        binding.ivProfilePic.setImageURI(Uri.parse(path))
                    }
                }
            }
        }

        binding.btnLogout.setOnClickListener {
            SessionManager.clearSession(requireContext())
            findNavController().navigate(com.android.dailymood.R.id.action_homeFragment_to_loginFragment)
        }

        binding.btnEdit.setOnClickListener {
            EditProfileDialogFragment().show(parentFragmentManager, "editProfile")
        }

        binding.ivProfilePic.setOnClickListener {
            showImageOptions()
        }

        return binding.root
    }

    private fun showImageOptions() {
        val options = arrayOf("Selecionar da Galeria", "Tirar Foto")
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Atualizar Foto de Perfil")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> pickImageFromGallery()
                    1 -> checkCameraPermissionAndLaunch()
                }
            }.show()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun checkCameraPermissionAndLaunch() {
        val permission = android.Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(requireContext(), permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(permission), REQUEST_CAMERA_PERMISSION)
        } else {
            takePhotoWithCamera()
        }
    }

    private fun takePhotoWithCamera() {
        val file = File(
            requireContext().getExternalFilesDir(null),
            "profile_pic_${System.currentTimeMillis()}.jpg"
        )
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            file
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        takePhotoLauncher.launch(intent)
    }

    private fun updatePhoto(uri: Uri) {
        val uriString = uri.toString()
        val userId = SessionManager.getLoggedInUser(requireContext()) ?: return
        val db = AppDatabase.getDatabase(requireContext())

        binding.ivProfilePic.setImageURI(uri)

        scope.launch {
            val user = db.userDao().getUserById(userId)
            if (user != null) {
                val updatedUser = user.copy(photoUri = uriString)
                db.userDao().updateUser(updatedUser)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhotoWithCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissão de câmera negada",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scope.cancel()
    }
}
