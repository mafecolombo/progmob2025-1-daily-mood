package com.android.dailymood.ui.fragments.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.android.dailymood.R
import com.android.dailymood.data.local.dao.AppDatabase
import com.android.dailymood.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileDialogFragment : DialogFragment() {

    private lateinit var etNewName: EditText
    private lateinit var etNewEmail: EditText
    private lateinit var btnConfirm: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_edit_profile, null)

        etNewName = view.findViewById(R.id.etNewName)
        etNewEmail = view.findViewById(R.id.etNewEmail)
        btnConfirm = view.findViewById(R.id.btnConfirm)

        val context = requireContext()
        val userId = SessionManager.getLoggedInUser(context) ?: return super.onCreateDialog(savedInstanceState)
        val db = AppDatabase.getDatabase(context)

        btnConfirm.setOnClickListener {
            val newName = etNewName.text.toString()
            val newEmail = etNewEmail.text.toString()

            if (newName.isNotBlank() && newEmail.isNotBlank()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val user = db.userDao().getUserById(userId)
                    if (user != null) {
                        val updatedUser = user.copy(name = newName, email = newEmail)
                        db.userDao().updateUser(updatedUser)

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Perfil atualizado", Toast.LENGTH_SHORT).show()
                            dismiss()
                        }
                    }
                }
            }
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Editar Perfil")
            .setView(view)
            .create()
    }
}
